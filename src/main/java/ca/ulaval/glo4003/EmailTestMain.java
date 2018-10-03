package ca.ulaval.glo4003;

import ca.ulaval.glo4003.domain.notification.Notification;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.infrastructure.notification.EmailNotificationSender;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;

public class EmailTestMain {
  public static void main(String[] args) {
    AmazonSimpleEmailService client =
        AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
    NotificationSender emailSender = new EmailNotificationSender(client);

    Notification notification = new Notification("Test", "sum", "body");
    User user = new User("Archi.test.42@gmail.com", "password", UserRole.INVESTOR);
    emailSender.sendNotification(notification, user);
  }
}
