package ca.ulaval.glo4003.infrastructure.notification;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.notification.Notification;
import ca.ulaval.glo4003.domain.notification.NotificationCoordinates;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AwsEmailNotificationSenderTest {

  private static final Notification SOME_NOTIFICATION = new Notification("title", "message");
  private static final NotificationCoordinates SOME_NOTIFICATION_COORDINATES = new NotificationCoordinates("email@foo.com");
  @Mock
  private AmazonSimpleEmailService emailService;
  private AwsEmailNotificationSender notificationSender;

  @Before
  public void setup() {
    notificationSender = new AwsEmailNotificationSender(emailService);
  }

  @Test
  public void whenSendingNotification_thenEmailIsSent() {
    notificationSender.sendNotification(SOME_NOTIFICATION, SOME_NOTIFICATION_COORDINATES);

    verify(emailService).sendEmail(any());
  }

  @Test
  public void givenException_whenSendingNotification_thenNothingHappens() {
    given(emailService.sendEmail(any(SendEmailRequest.class))).willThrow(SdkClientException.class);

    notificationSender.sendNotification(SOME_NOTIFICATION, SOME_NOTIFICATION_COORDINATES);
  }
}
