package ca.ulaval.glo4003;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import ca.ulaval.glo4003.infrastructure.clock.ClockDriver;
import ca.ulaval.glo4003.infrastructure.market.MarketCsvLoader;
import ca.ulaval.glo4003.infrastructure.market.MarketsUpdater;
import ca.ulaval.glo4003.infrastructure.stock.StockCsvLoader;
import ca.ulaval.glo4003.ws.http.CORSResponseFilter;
import ca.ulaval.glo4003.ws.infrastructure.config.ServiceLocatorInitializer;
import ca.ulaval.glo4003.ws.infrastructure.config.SimulationSettings;
import ca.ulaval.glo4003.ws.infrastructure.injection.ErrorMapper;
import ca.ulaval.glo4003.ws.infrastructure.injection.ServiceLocator;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Resource;
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

  private static Server server;
  private static ClockDriver clockDriver;

  public static void main(String[] args) throws Exception {
    ServiceLocator serviceLocator = new ServiceLocator();
    new ServiceLocatorInitializer().initializeServiceLocator(serviceLocator);

    loadData(serviceLocator);
    startClockAndMarketsUpdater(serviceLocator);

    int port = 8080;
    ContextHandlerCollection contexts = new ContextHandlerCollection();
    contexts.setHandlers(new Handler[] {createApiHandler(serviceLocator), createUiHandler()});
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

  public static void stop() throws Exception {
    server.stop();
  }

  static boolean isStarted() {
    return server != null && server.isStarted();
  }

  private static Handler createApiHandler(ServiceLocator serviceLocator) {

    Application application = new Application() {
      @Override
      public Set<Object> getSingletons() {
        return createInstances(serviceLocator);
      }
    };

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/api/");
    ResourceConfig resourceConfig = ResourceConfig.forApplication(application);
    resourceConfig.register(CORSResponseFilter.class);

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

  private static Set<Object> createInstances(ServiceLocator serviceLocator) {
    Set<?> resourceInstances = serviceLocator.getAllClassesForAnnotation(Resource.class);
    Set<?> errorMapperClasses = serviceLocator.getAllClassesForAnnotation(ErrorMapper.class);
    return Stream.of(resourceInstances, errorMapperClasses, Collections.singletonList(new OpenApiResource()))
        .flatMap(Collection::stream)
        .collect(toSet());
  }

  private static void loadData(ServiceLocator serviceLocator) {
    try {
      MarketCsvLoader marketLoader = new MarketCsvLoader(serviceLocator.get(MarketRepository.class),
          serviceLocator.get(StockRepository.class),
          serviceLocator.get(StockValueRetriever.class));
      marketLoader.load();

      StockCsvLoader stockLoader = new StockCsvLoader(serviceLocator.get(StockRepository.class),
          serviceLocator.get(MarketRepository.class));
      stockLoader.load();
    } catch (IOException e) {
      System.out.println("Unable to parse the CSV: " + e.getMessage());
    }
  }

  private static void startClockAndMarketsUpdater(ServiceLocator serviceLocator) {
    serviceLocator.registerInstance(Clock.class, new Clock(LocalDateTime.of(2018, 9, 15, 0, 0, 0), SimulationSettings.CLOCK_TICK_DURATION));
    clockDriver = new ClockDriver(serviceLocator.get(Clock.class), SimulationSettings.SIMULATION_UPDATE_FREQUENCY);
    new MarketsUpdater(serviceLocator.get(Clock.class), serviceLocator.get(MarketRepository.class));
    clockDriver.start();
  }
}
