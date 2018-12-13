package ca.ulaval.glo4003.context;

import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocatorInitializer;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

public abstract class AbstractContext {

  protected final ServiceLocator serviceLocator;

  protected final JerseyApiHandlersCreator jerseyApiHandlersCreator;

  public AbstractContext(JerseyApiHandlersCreator jerseyApiHandlersCreator) {
    this.jerseyApiHandlersCreator = jerseyApiHandlersCreator;
    serviceLocator = ServiceLocator.INSTANCE;
  }

  public abstract void configureDestruction();

  public abstract ContextHandlerCollection createJettyContextHandlers();

  public void configureApplication(String apiUrl) {
    createServiceLocatorInitializer().configure();
    loadData();
  }

  protected abstract ServiceLocatorInitializer createServiceLocatorInitializer();

  protected abstract void loadData();
}
