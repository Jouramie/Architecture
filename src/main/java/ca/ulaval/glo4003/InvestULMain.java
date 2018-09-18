package ca.ulaval.glo4003;

import ca.ulaval.glo4003.ws.api.PingResource;
import ca.ulaval.glo4003.ws.http.CORSResponseFilter;

import java.util.HashSet;
import java.util.Set;

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

    public static void main(String[] args) throws Exception {
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] {createApiHandler(), createUiHandler()});
        server = new Server(8080);
        server.setHandler(contexts);

        try {
            server.start();
            server.join();
        } finally {
            server.stop();
            server.destroy();
        }
    }

    public static void stop() throws Exception {
        try{
            server.stop();
        } finally {
            server.destroy();
        }
    }

    public static boolean isStarted() {
        if(server == null) {
            return false;
        }
        return server.isStarted();
    }

    private static Handler createApiHandler() {
        PingResource pingResource = new PingResource();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/api/");
        ResourceConfig resourceConfig = ResourceConfig.forApplication(new Application() {
            @Override
            public Set<Object> getSingletons() {
                HashSet<Object> resources = new HashSet<>();

                // Add resources to context
                resources.add(pingResource);

                return resources;
            }
        });
        resourceConfig.register(CORSResponseFilter.class);

        ServletContainer servletContainer = new ServletContainer(resourceConfig);
        ServletHolder servletHolder = new ServletHolder(servletContainer);
        context.addServlet(servletHolder, "/*");

        return context;
    }

    private static Handler createUiHandler() {
        WebAppContext webapp = new WebAppContext();
        webapp.setResourceBase("src/main/webapp");
        webapp.setContextPath("/");
        return webapp;
    }
}
