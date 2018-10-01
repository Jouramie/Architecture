package ca.ulaval.glo4003;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.ws.http.CORSResponseFilter;
import ca.ulaval.glo4003.ws.infrastructure.config.ServiceLocatorInitializer;
import ca.ulaval.glo4003.ws.infrastructure.injection.FilterRegistration;
import ca.ulaval.glo4003.ws.infrastructure.injection.ServiceLocator;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.net.URL;
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

  private static final String WEB_SERVICE_PACKAGE_PREFIX = "ca.ulaval.glo4003.ws";
  private static Server server;

  public static void main(String[] args) throws Exception {
    int port = 8080;
    ServiceLocatorInitializer serviceLocatorInitializer
        = new ServiceLocatorInitializer(WEB_SERVICE_PACKAGE_PREFIX);
    serviceLocatorInitializer.initializeServiceLocator(ServiceLocator.INSTANCE);

    ContextHandlerCollection contexts = new ContextHandlerCollection();
    contexts.setHandlers(new Handler[] {createApiHandler(serviceLocatorInitializer), createUiHandler()});


    server = new Server(port);
    server.setHandler(contexts);

    URL serverUrl = server.getURI().toURL();
    URL apiUrl = new URL(serverUrl.getProtocol(), serverUrl.getHost(), port, serverUrl.getFile());
    createSwaggerApi(apiUrl.toString());

    try {
      server.start();
      server.join();
    } finally {
      server.stop();
      server.destroy();
    }
  }

  public static void stop() throws Exception {
    try {
      server.stop();
    } finally {
      server.destroy();
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
}
