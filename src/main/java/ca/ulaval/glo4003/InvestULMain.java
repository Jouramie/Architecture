package ca.ulaval.glo4003;

import ca.ulaval.glo4003.context.AbstractContext;
import ca.ulaval.glo4003.context.DemoContext;
import ca.ulaval.glo4003.context.ProductionContext;
import ca.ulaval.glo4003.context.SwaggerApiHandlerCreator;
import java.net.URL;
import org.eclipse.jetty.server.Server;

public class InvestULMain {

  private static Server server;

  public static void main(String[] args) throws Exception {
    AbstractContext context = new DemoContext(new SwaggerApiHandlerCreator());
    startServer(context);
  }

  public static void startServer(AbstractContext context) throws Exception {
    int port = 8080;
    server = new Server(port);

    URL serverUrl = server.getURI().toURL();
    URL apiUrl = new URL(serverUrl.getProtocol(), serverUrl.getHost(), port, serverUrl.getFile());

    context.configureApplication(apiUrl.toString() + "api");
    server.setHandler(context.createJettyContextHandlers());

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

  public static boolean isStarted() {
    return server != null && server.isStarted();
  }
}
