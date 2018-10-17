package ca.ulaval.glo4003.context;

import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.notification.NotificationSenderStub;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;

public class StoppedClockContext extends AbstractContext {

  public StoppedClockContext(String webServicePackagePrefix, ServiceLocator serviceLocator) {
    super(webServicePackagePrefix, serviceLocator);
  }

  @Override
  public void configureApplication(String apiUrl) {
    super.configureApplication(apiUrl);
    serviceLocator.register(NotificationSender.class, NotificationSenderStub.class);
  }
}
