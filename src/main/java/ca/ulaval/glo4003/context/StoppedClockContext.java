package ca.ulaval.glo4003.context;

import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.notification.NullNotificationSender;

public class StoppedClockContext extends AbstractContext {

  @Override
  public void configureApplication(String apiUrl) {
    super.configureApplication(apiUrl);
    serviceLocator.register(NotificationSender.class, NullNotificationSender.class);
  }
}
