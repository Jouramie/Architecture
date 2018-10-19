package ca.ulaval.glo4003.domain.notification;

import ca.ulaval.glo4003.domain.user.User;

public class NullNotificationSender implements NotificationSender {
  @Override
  public void sendNotification(Notification notification, User user) {
    // Null method stub
  }
}
