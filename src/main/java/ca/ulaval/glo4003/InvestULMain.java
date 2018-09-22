package ca.ulaval.glo4003;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import ca.ulaval.glo4003.ws.api.PingResource;
import ca.ulaval.glo4003.ws.api.PingResourceImpl;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationResource;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationResourceImpl;
import ca.ulaval.glo4003.ws.api.authentication.UserAlreadyExistsExceptionMapper;
import ca.ulaval.glo4003.ws.api.authentication.UserResource;
import ca.ulaval.glo4003.ws.api.authentication.UserResourceImpl;
import ca.ulaval.glo4003.ws.http.CORSResponseFilter;
import ca.ulaval.glo4003.ws.infrastructure.config.ServiceLocatorInitializer;
import ca.ulaval.glo4003.ws.infrastructure.injection.ErrorMapper;
import ca.ulaval.glo4003.ws.infrastructure.injection.ServiceLocator;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.ws.rs.core.Application;

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
import org.reflections.Reflections;

public class InvestULMain {

    private static Server server;

    public static void main(String[] args) throws Exception {
        final int port = 8080;

        ServiceLocator serviceLocator = new ServiceLocator();
        new ServiceLocatorInitializer().initializeServiceLocator(serviceLocator);

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

    public static boolean isStarted() {
        return server != null && server.isStarted();
    }

    private static Handler createApiHandler(ServiceLocator serviceLocator) {
        List<?> resourceInstances = getResourceClasses().stream().map(serviceLocator::get).collect(toList());
        List<?> errorMapperClasses = getErrorMapperClasses().stream().map(serviceLocator::get).collect(toList());

        Set<Object> instances = Stream.of(resourceInstances, errorMapperClasses, Collections.singletonList(new OpenApiResource())).flatMap(Collection::stream).collect(toSet());

        Application application = new Application() {
            @Override
            public Set<Object> getSingletons() {
                return instances;
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

    private static Set<Class<?>> getResourceClasses() {
        return new Reflections("ca.ulaval.glo4003.ws").getTypesAnnotatedWith(Resource.class);
    }

    private static Collection<Class<?>> getErrorMapperClasses() {
        return new Reflections("ca.ulaval.glo4003.ws").getTypesAnnotatedWith(ErrorMapper.class);
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
