package ca.ulaval.glo4003;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import ca.ulaval.glo4003.infrastructure.clock.ClockDriver;
import ca.ulaval.glo4003.infrastructure.config.ServiceLocatorInitializer;
import ca.ulaval.glo4003.infrastructure.config.SimulationSettings;
import ca.ulaval.glo4003.infrastructure.injection.FilterRegistration;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.infrastructure.market.MarketCsvLoader;
import ca.ulaval.glo4003.infrastructure.market.MarketsUpdater;
import ca.ulaval.glo4003.infrastructure.stock.StockCsvLoader;
import ca.ulaval.glo4003.ws.http.CORSResponseFilter;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
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
    ServiceLocatorInitializer serviceLocatorInitializer = new ServiceLocatorInitializer(WEB_SERVICE_PACKAGE_PREFIX);
    serviceLocatorInitializer.initializeServiceLocator(ServiceLocator.INSTANCE);

    loadData();
    startClockAndMarketsUpdater();

    ContextHandlerCollection contexts = new ContextHandlerCollection();
    contexts.setHandlers(new Handler[] {createApiHandler(serviceLocatorInitializer), createUiHandler()});

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
    ServiceLocator.INSTANCE.getClassesForAnnotation(WEB_SERVICE_PACKAGE_PREFIX, FilterRegistration.class)
        .forEach(resourceConfig::register);

    ServletContainer servletContainer = new ServletContainer(resourceConfig);
    ServletHolder servletHolder = new ServletHolder(servletContainer);
    context.addServlet(servletHolder, "/*");

    return context;
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

  private static void loadData() {
    try {
      MarketCsvLoader marketLoader = new MarketCsvLoader(ServiceLocator.INSTANCE.get(MarketRepository.class),
          ServiceLocator.INSTANCE.get(StockRepository.class),
          ServiceLocator.INSTANCE.get(StockValueRetriever.class));
      marketLoader.load();

      StockCsvLoader stockLoader = new StockCsvLoader(ServiceLocator.INSTANCE.get(StockRepository.class),
          ServiceLocator.INSTANCE.get(MarketRepository.class));
      stockLoader.load();
    } catch (IOException e) {
      System.out.println("Unable to parse the CSV: " + e.getMessage());
    }
  }

  private static void startClockAndMarketsUpdater() {
    ServiceLocator.INSTANCE.registerInstance(Clock.class, new Clock(LocalDateTime.of(2018, 9, 15, 0, 0, 0), SimulationSettings.CLOCK_TICK_DURATION));
    clockDriver = new ClockDriver(ServiceLocator.INSTANCE.get(Clock.class), SimulationSettings.SIMULATION_UPDATE_FREQUENCY);
    new MarketsUpdater(ServiceLocator.INSTANCE.get(Clock.class), ServiceLocator.INSTANCE.get(MarketRepository.class));
    clockDriver.start();
  }
}
