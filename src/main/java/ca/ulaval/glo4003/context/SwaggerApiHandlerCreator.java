package ca.ulaval.glo4003.context;

import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import java.util.List;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.webapp.WebAppContext;

public class SwaggerApiHandlerCreator extends JerseyApiHandlersCreator {

  @Override
  public List<Handler> createHandlers(ServiceLocator serviceLocator, String webServicePackagePrefix) {
    List<Handler> handlers = super.createHandlers(serviceLocator, webServicePackagePrefix);
    handlers.add(createSwaggerUiHandler());
    return handlers;
  }

  private Handler createSwaggerUiHandler() {
    WebAppContext webapp = new WebAppContext();
    webapp.setResourceBase("src/main/webapp");
    webapp.setContextPath("/");
    return webapp;
  }
}
