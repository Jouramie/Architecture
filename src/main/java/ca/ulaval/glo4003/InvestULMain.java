package ca.ulaval.glo4003;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.infrastructure.clock.ClockDriver;
import ca.ulaval.glo4003.infrastructure.config.ServiceLocatorInitializer;
import ca.ulaval.glo4003.infrastructure.config.SimulationSettings;
import ca.ulaval.glo4003.infrastructure.injection.FilterRegistration;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.infrastructure.market.MarketCsvLoader;
import ca.ulaval.glo4003.infrastructure.market.MarketUpdater;
import ca.ulaval.glo4003.infrastructure.notification.EmailNotificationSender;
import ca.ulaval.glo4003.infrastructure.stock.StockCsvLoader;
import ca.ulaval.glo4003.ws.http.CORSResponseFilter;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;
import javax.ws.rs.core.Application;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class InvestULMain {

  private static final String WEB_SERVICE_PACKAGE_PREFIX = "ca.ulaval.glo4003";
  private static Server server;
  private static ClockDriver clockDriver;

  public static void main(String[] args) throws Exception {
    ServiceLocatorInitializer serviceLocatorInitializer
        = new ServiceLocatorInitializer(WEB_SERVICE_PACKAGE_PREFIX);
    serviceLocatorInitializer.initialize(ServiceLocator.INSTANCE);
    createAwsSesSender();
    hardcodeTestUser();

    loadData();
    LocalDate startDate = getStartDate();
    startClockAndMarketsUpdater(startDate);

    ContextHandlerCollection contexts = new ContextHandlerCollection();
    contexts.setHandlers(new Handler[] {
        createApiHandler(serviceLocatorInitializer),
        createUiHandler()
    });

    int port = 8080;
    server = new Server(port);
    server.setHandler(contexts);

    URL serverUrl = server.getURI().toURL();
    URL apiUrl = new URL(serverUrl.getProtocol(), serverUrl.getHost(), port, serverUrl.getFile());
    createSwaggerApi(apiUrl.toString());

    try {
      server.start();
      server.join();
    } finally {
      clockDriver.stop();
      server.stop();
      server.destroy();
    }
  }

  public static void stop() {
    try {
      server.stop();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  static boolean isStarted() {
    return server != null && server.isStarted();
  }

  private static Handler createApiHandler(ServiceLocatorInitializer serviceLocatorInitializer) {

    Application application = new Application() {
      @Override
      public Set<Object> getSingletons() {
        return serviceLocatorInitializer.createInstances(ServiceLocator.INSTANCE);
      }
    };

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/api/");
    ResourceConfig resourceConfig = ResourceConfig.forApplication(application);
    resourceConfig.register(CORSResponseFilter.class);
    setupJacksonJavaTimeModule(resourceConfig);

    ServiceLocator.INSTANCE.getClassesForAnnotation(
        WEB_SERVICE_PACKAGE_PREFIX,
        FilterRegistration.class)
        .forEach(resourceConfig::register);

    ServletContainer servletContainer = new ServletContainer(resourceConfig);
    ServletHolder servletHolder = new ServletHolder(servletContainer);
    context.addServlet(servletHolder, "/*");

    return context;
  }

  private static void setupJacksonJavaTimeModule(ResourceConfig resourceConfig) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
    JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
    provider.setMapper(mapper);
    resourceConfig.register(provider);
  }

  private static void createSwaggerApi(String apiUrl) {
    OpenAPI oas = new OpenAPI();
    Info info = new Info()
        .title("Invest-UL")
        .description("Logiciel transactionnel pour titres boursiers");

    io.swagger.v3.oas.models.servers.Server server = new io.swagger.v3.oas.models.servers.Server();
    server.setUrl(apiUrl);

    oas
        .info(info)
        .servers(Stream.of(server).collect(toList()));

    SwaggerConfiguration oasConfig = new SwaggerConfiguration()
        .openAPI(oas)
        .prettyPrint(true);

    try {
      new JaxrsOpenApiContextBuilder()
          .openApiConfiguration(oasConfig)
          .buildContext(true);
    } catch (OpenApiConfigurationException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private static Handler createUiHandler() {
    WebAppContext webapp = new WebAppContext();
    webapp.setResourceBase("src/main/webapp");
    webapp.setContextPath("/");
    return webapp;
  }

  private static void createAwsSesSender() {
    AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
        .withRegion(Regions.US_EAST_1).build();
    NotificationSender emailSender = new EmailNotificationSender(client);
    ServiceLocator.INSTANCE.registerInstance(NotificationSender.class, emailSender);
  }

  private static void hardcodeTestUser() {
    String testEmail = "Archi.test.42@gmail.com";
    try {
      ServiceLocator.INSTANCE.get(UserRepository.class)
          .add(new User(testEmail, "asdf", UserRole.ADMINISTRATOR));
    } catch (UserAlreadyExistsException exception) {
      System.out.println("Test user couldn't be added");
      exception.printStackTrace();
    }
    ServiceLocator.INSTANCE.get(AuthenticationTokenRepository.class)
        .add(new AuthenticationToken("00000000-0000-0000-0000-000000000000", testEmail));
  }

  private static void loadData() throws IOException, MarketNotFoundException {
    MarketCsvLoader marketLoader = new MarketCsvLoader(
        ServiceLocator.INSTANCE.get(MarketRepository.class),
        ServiceLocator.INSTANCE.get(StockRepository.class),
        ServiceLocator.INSTANCE.get(StockValueRetriever.class));
    marketLoader.load();

    StockCsvLoader stockLoader = new StockCsvLoader(
        ServiceLocator.INSTANCE.get(StockRepository.class),
        ServiceLocator.INSTANCE.get(MarketRepository.class));
    stockLoader.load();
  }

  private static LocalDate getStartDate() {
    StockRepository stockRepository = ServiceLocator.INSTANCE.get(StockRepository.class);
    Stock stock = stockRepository.findAll().get(0);
    HistoricalStockValue latestStockValue = stock.getValueHistory().getLatestValue();

    return latestStockValue.date.plusDays(1);
  }

  private static void startClockAndMarketsUpdater(LocalDate startDate) {
    ServiceLocator.INSTANCE.registerInstance(
        Clock.class,
        new Clock(startDate.atTime(0, 0, 0),
            SimulationSettings.CLOCK_TICK_DURATION));
    clockDriver = new ClockDriver(
        ServiceLocator.INSTANCE.get(Clock.class),
        SimulationSettings.SIMULATION_UPDATE_FREQUENCY);
    new MarketUpdater(
        ServiceLocator.INSTANCE.get(Clock.class),
        ServiceLocator.INSTANCE.get(MarketRepository.class));
    clockDriver.start();
  }
}
