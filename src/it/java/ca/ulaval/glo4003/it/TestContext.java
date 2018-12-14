package ca.ulaval.glo4003.it;

import ca.ulaval.glo4003.context.JerseyApiHandlersCreator;
import ca.ulaval.glo4003.context.ProductionContext;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.notification.NullNotificationSender;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocatorInitializer;

public class TestContext extends ProductionContext {

  public TestContext(JerseyApiHandlersCreator jerseyApiHandlersCreator) {
    super(jerseyApiHandlersCreator);
  }

  @Override
  protected ServiceLocatorInitializer createServiceLocatorInitializer() {
    return super.createServiceLocatorInitializer()
        .registerInstance(NotificationSender.class, new NullNotificationSender());
  }
}
