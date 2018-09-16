package ca.ulaval.glo4003;

import ca.ulaval.glo4003.ws.api.PingResource;
import ca.ulaval.glo4003.ws.http.CORSResponseFilter;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.ws.rs.core.Application;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InvestULMain {

    public static void main(String[] args) throws Exception {
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] {createApiHandler(), createUiHandler()});
        Server server = new Server(8080);
        server.setHandler(contexts);

        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }

    private static Handler createApiHandler() {
        PingResource pingResource = new PingResource();
        Application application = new Application() {
            @Override
            public Set<Object> getSingletons() {
                return Stream.of(
                        pingResource,
                        new OpenApiResource() // Routes for Swagger integration
                ).collect(Collectors.toSet());
            }
        };

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/api/");
        ResourceConfig resourceConfig = ResourceConfig.forApplication(application);
        resourceConfig.register(CORSResponseFilter.class);

        ServletContainer servletContainer = new ServletContainer(resourceConfig);
        ServletHolder servletHolder = new ServletHolder(servletContainer);
        context.addServlet(servletHolder, "/*");

        createSwaggerApi(application);

        return context;
    }

    private static void createSwaggerApi(Application application) {
        OpenAPI oas = new OpenAPI();
        Info info = new Info()
                .title("Invest-UL")
                .description("Logiciel transactionnel pour titres boursiers");

        oas.info(info);
        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                .openAPI(oas)
                .prettyPrint(true);

        try {
            new JaxrsOpenApiContextBuilder()
                    .application(application)
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
