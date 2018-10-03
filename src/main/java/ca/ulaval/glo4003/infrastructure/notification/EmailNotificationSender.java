package ca.ulaval.glo4003.infrastructure.notification;

import ca.ulaval.glo4003.domain.notification.Notification;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.user.User;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;

public class EmailNotificationSender implements NotificationSender {

    public static final String EMAIL_FROM = "invest.ul.2018@gmail.com";
    public static final String UTF_8 = "UTF-8";

    AmazonSimpleEmailService emailService;

    public EmailNotificationSender(AmazonSimpleEmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void sendNotification(Notification notification, User user) {
        try {
            Message message = constructEmailMessage(notification);
            Destination destination = new Destination().withToAddresses(user.getUsername());
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(destination)
                    .withMessage(message)
                    .withSource(EMAIL_FROM);
            emailService.sendEmail(request);
        } catch (Exception ex) {
            System.out.println("The email was not sent. Error message: " + ex.getMessage());
        }
    }

    private Message constructEmailMessage(Notification notification) {
        return new Message()
                .withBody(new Body()
                        .withText(new Content()
                            .withCharset(UTF_8).withData(notification.message)))
                .withSubject(new Content()
                        .withCharset(UTF_8).withData(notification.title));
    }
}
