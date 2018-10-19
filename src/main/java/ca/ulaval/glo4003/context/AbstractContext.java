package ca.ulaval.glo4003.context;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import ca.ulaval.glo4003.domain.transaction.NullPaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.PaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.TransactionLedger;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.infrastructure.config.SimulationSettings;
import ca.ulaval.glo4003.infrastructure.injection.Component;
import ca.ulaval.glo4003.infrastructure.injection.ErrorMapper;
import ca.ulaval.glo4003.infrastructure.injection.FilterRegistration;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.infrastructure.market.MarketCsvLoader;
import ca.ulaval.glo4003.infrastructure.persistence.InMemoryAuthenticationTokenRepository;
import ca.ulaval.glo4003.infrastructure.persistence.InMemoryMarketRepository;
import ca.ulaval.glo4003.infrastructure.persistence.InMemoryStockRepository;
import ca.ulaval.glo4003.infrastructure.persistence.InMemoryTransactionLedger;
import ca.ulaval.glo4003.infrastructure.persistence.InMemoryUserRepository;
import ca.ulaval.glo4003.infrastructure.stock.SimulatedStockValueRetriever;
import ca.ulaval.glo4003.infrastructure.stock.StockCsvLoader;
import ca.ulaval.glo4003.ws.http.CORSResponseFilter;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Resource;
import javax.ws.rs.core.Application;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public abstract class AbstractContext {

  protected final String webServicePackagePrefix;
  protected final ServiceLocator serviceLocator;

  public AbstractContext(String webServicePackagePrefix, ServiceLocator serviceLocator) {
    this.webServicePackagePrefix = webServicePackagePrefix;
    this.serviceLocator = serviceLocator;
  }

  public void configureApplication(String apiUrl) {
    initializeServiceLocator();
    loadData();
    initializeClock();
    createSwaggerApi(apiUrl);
  }

  protected void initializeServiceLocator() {
    serviceLocator.discoverPackage(webServicePackagePrefix, Resource.class, ErrorMapper.class,
        Component.class, FilterRegistration.class);
    serviceLocator.registerInstance(OpenApiResource.class, new OpenApiResource());
    serviceLocator.registerSingleton(UserRepository.class, InMemoryUserRepository.class);
    serviceLocator.registerSingleton(AuthenticationTokenRepository.class,
        InMemoryAuthenticationTokenRepository.class);
    serviceLocator.registerSingleton(StockRepository.class, InMemoryStockRepository.class);
    serviceLocator.registerSingleton(MarketRepository.class, InMemoryMarketRepository.class);
    serviceLocator.registerSingleton(StockValueRetriever.class, SimulatedStockValueRetriever.class);
    serviceLocator.registerSingleton(TransactionLedger.class, InMemoryTransactionLedger.class);
    serviceLocator.registerSingleton(PaymentProcessor.class, NullPaymentProcessor.class);
  }

  protected Set<Object> createRegisteredComponentInstances() {
    List<Class<?>> registeredClasses = Stream.of(Resource.class, ErrorMapper.class, Component.class)
        .map(annotation -> serviceLocator.getClassesForAnnotation(webServicePackagePrefix, annotation))
        .flatMap(Collection::stream).collect(toList());
    registeredClasses.add(OpenApiResource.class);

    return registeredClasses.stream()
        .map(serviceLocator::get)
        .collect(toSet());
  }

  protected void loadData() {
    try {
      loadCsvData();
      createAdministrator();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void createAdministrator() {
    String testEmail = "Archi.test.42@gmail.com";
    try {
      serviceLocator.get(UserRepository.class)
          .add(new User(testEmail, "asdf", UserRole.ADMINISTRATOR));
    } catch (UserAlreadyExistsException exception) {
      System.out.println("Test user couldn't be added");
      exception.printStackTrace();
    }
    serviceLocator.get(AuthenticationTokenRepository.class)
        .add(new AuthenticationToken("00000000-0000-0000-0000-000000000000", testEmail));
  }

  private void loadCsvData() throws IOException, MarketNotFoundException {
    MarketCsvLoader marketLoader = new MarketCsvLoader(
        serviceLocator.get(MarketRepository.class),
        serviceLocator.get(StockRepository.class),
        serviceLocator.get(StockValueRetriever.class));
    marketLoader.load();

    StockCsvLoader stockLoader = new StockCsvLoader(
        serviceLocator.get(StockRepository.class),
        serviceLocator.get(MarketRepository.class));
    stockLoader.load();
  }

  private void initializeClock() {
    StockRepository stockRepository = serviceLocator.get(StockRepository.class);
    Stock stock = stockRepository.getAll().get(0);
    HistoricalStockValue latestStockValue = stock.getValueHistory().getLatestValue();

    LocalDate startDate = latestStockValue.date.plusDays(1);

    serviceLocator.registerInstance(
        Clock.class,
        new Clock(startDate.atTime(0, 0, 0),
            SimulationSettings.CLOCK_TICK_DURATION));
  }

  public void configureDestruction() {
  }

  public ContextHandlerCollection createJettyContextHandlers() {
    ContextHandlerCollection contexts = new ContextHandlerCollection();
    contexts.setHandlers(new Handler[] {
        createApiHandler(),
        createSwaggerUiHandler()
    });

    return contexts;
  }

  private Handler createApiHandler() {
    Application application = new Application() {
      @Override
      public Set<Object> getSingletons() {
        return createRegisteredComponentInstances();
      }
    };

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/api/");
    ResourceConfig resourceConfig = ResourceConfig.forApplication(application);
    resourceConfig.register(CORSResponseFilter.class);
    serviceLocator.getClassesForAnnotation(
        webServicePackagePrefix,
        FilterRegistration.class)
        .forEach(resourceConfig::register);

    ServletContainer servletContainer = new ServletContainer(resourceConfig);
    ServletHolder servletHolder = new ServletHolder(servletContainer);
    context.addServlet(servletHolder, "/*");

    return context;
  }

  private Handler createSwaggerUiHandler() {
    WebAppContext webapp = new WebAppContext();
    webapp.setResourceBase("src/main/webapp");
    webapp.setContextPath("/");
    return webapp;
  }

  private void createSwaggerApi(String apiUrl) {
    OpenAPI swaggerOpenApi = new OpenAPI();
    Info info = new Info()
        .title("Invest-UL")
        .description("Logiciel transactionnel pour titres boursiers");

    Server server = new Server();
    server.setUrl(apiUrl);

    swaggerOpenApi
        .info(info)
        .servers(Stream.of(server).collect(toList()));

    SwaggerConfiguration oasConfig = new SwaggerConfiguration()
        .openAPI(swaggerOpenApi)
        .prettyPrint(true);

    try {
      new JaxrsOpenApiContextBuilder()
          .openApiConfiguration(oasConfig)
          .buildContext(true);
    } catch (OpenApiConfigurationException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
