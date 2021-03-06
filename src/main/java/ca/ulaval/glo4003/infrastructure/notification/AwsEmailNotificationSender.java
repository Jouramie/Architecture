package ca.ulaval.glo4003.infrastructure.notification;

import ca.ulaval.glo4003.domain.notification.Notification;
import ca.ulaval.glo4003.domain.notification.NotificationCoordinates;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AwsEmailNotificationSender implements NotificationSender {

  private static final Logger logger = LoggerFactory.getLogger(AwsEmailNotificationSender.class);

  private static final String EMAIL_FROM = "invest.ul.2018@gmail.com";
  private static final String UTF_8 = "UTF-8";

  private final AmazonSimpleEmailService emailService;

  @Inject
  public AwsEmailNotificationSender(AmazonSimpleEmailService emailService) {
    this.emailService = emailService;
  }

  @Override
  public void sendNotification(Notification notification, NotificationCoordinates coordinates) {
    try {
      Message message = constructEmailMessage(notification);
      Destination destination = new Destination().withToAddresses(coordinates.email);
      SendEmailRequest request = new SendEmailRequest()
          .withDestination(destination)
          .withMessage(message)
          .withSource(EMAIL_FROM);
      emailService.sendEmail(request);
    } catch (SdkClientException exception) {
      logger.error("Could not send email to " + coordinates.email);
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
