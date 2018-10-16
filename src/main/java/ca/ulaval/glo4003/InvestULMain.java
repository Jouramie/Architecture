package ca.ulaval.glo4003;

import ca.ulaval.glo4003.context.AbstractContext;
import ca.ulaval.glo4003.context.ProductionContext;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import java.net.URL;
import org.eclipse.jetty.server.Server;

public class InvestULMain {

  private static final String WEB_SERVICE_PACKAGE_PREFIX = "ca.ulaval.glo4003";
  private static Server server;


  public static void main(String[] args) throws Exception {

    // TODO add possibility to load other context
    AbstractContext context = new ProductionContext(WEB_SERVICE_PACKAGE_PREFIX, ServiceLocator.INSTANCE);
    context.configureApplication();

    int port = 8080;
    server = new Server(port);
    server.setHandler(context.createJettyContextHandlers());

    URL serverUrl = server.getURI().toURL();
    URL apiUrl = new URL(serverUrl.getProtocol(), serverUrl.getHost(), port, serverUrl.getFile());
    context.createSwaggerApi(apiUrl.toString());

    try {
      server.start();
      server.join();
    } finally {
      context.configureDestruction();
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
}
