package ca.ulaval.glo4003.domain.notification;

import ca.ulaval.glo4003.domain.user.User;

public interface NotificationSender {

  void sendNotification(Notification notification, User user);
}
