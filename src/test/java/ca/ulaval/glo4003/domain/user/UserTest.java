package ca.ulaval.glo4003.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.notification.Notification;
import ca.ulaval.glo4003.domain.notification.NotificationFactory;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.transaction.PaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionFactory;
import ca.ulaval.glo4003.domain.user.exceptions.EmptyCartException;
import ca.ulaval.glo4003.util.UserBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {

  private static final String SOME_EMAIL = "4email@email.com";
  private static final String SOME_PASSWORD = "a password";
  private static final String WRONG_PASSWORD = SOME_PASSWORD + "wrong";
  private static final String SOME_TITLE = "MSFT";
  private static final int SOME_QTY = 2;

  @Mock
  private StockRepository stockRepository;
  @Mock
  private TransactionFactory transactionFactory;
  @Mock
  private PaymentProcessor paymentProcessor;
  @Mock
  private NotificationFactory notificationFactory;
  @Mock
  private NotificationSender notificationSender;
  @Mock
  private Transaction transaction;
  @Mock
  private Notification notification;

  private User user;

  @Before
  public void setup() throws StockNotFoundException {
    given(stockRepository.doesStockExist(SOME_TITLE)).willReturn(true);
    user = new UserBuilder().withEmail(SOME_EMAIL).withPassword(SOME_PASSWORD).build();
    user.getCart().add(SOME_TITLE, SOME_QTY, stockRepository);

    given(transactionFactory.createPurchase(user.getCart())).willReturn(transaction);
    given(notificationFactory.create(transaction)).willReturn(notification);
  }

  @Test
  public void givenRightPassword_whenCheckingIfPasswordBelongsToUser_thenItDoes() {
    assertThat(user.isThisYourPassword(SOME_PASSWORD)).isTrue();
  }

  @Test
  public void givenWrongPassword_whenCheckingIfPasswordBelongsToUser_thenItDoesNot() {
    assertThat(user.isThisYourPassword(WRONG_PASSWORD)).isFalse();
  }

  @Test
  public void whenCreatingUser_thenCartIsEmpty() {
    User user = new User(SOME_EMAIL, SOME_PASSWORD, UserRole.ADMINISTRATOR);

    assertThat(user.getCart().isEmpty()).isTrue();
  }

  @Test
  public void whenCreatingUser_thenUserDoesNotOwnStock() {
    assertThat(user.getPortfolio().getStocks().isEmpty()).isTrue();
  }

  @Test
  public void whenCheckoutCart_thenPaymentIsProcessedWithTheCurrentTransaction() throws StockNotFoundException, EmptyCartException {
    user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender);

    verify(paymentProcessor).payment(transaction);
  }

  @Test
  public void whenCheckoutCart_thenANotificationIsSent() throws StockNotFoundException, EmptyCartException {
    user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender);

    verify(notificationSender).sendNotification(notification, user);
  }

  /*@Test
  public void whenCheckoutCart_thenPreviousCartContentIsReturned() throws StockNotFoundException, EmptyCartException {
    /*Transaction transaction = user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender);

    transaction.

        assertThat(transactionDto).isEqualTo(expectedDto);
  }*/

  @Test
  public void whenCheckoutCart_thenCartIsCleared() throws StockNotFoundException, EmptyCartException {
    user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender);

    assertThat(user.getCart().isEmpty());
  }

  @Test
  public void givenEmptyCart_whenCheckoutCart_thenThrowCheckoutEmptyCartException() {
    user.getCart().empty();

    assertThatThrownBy(() ->
        user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender))
        .isInstanceOf(EmptyCartException.class);

    verify(paymentProcessor, never()).payment(any());
    verify(notificationSender, never()).sendNotification(any(), any());
    assertThat(user.getPortfolio().getStocks().isEmpty());
  }
}
