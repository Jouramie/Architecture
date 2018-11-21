package ca.ulaval.glo4003.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
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
import ca.ulaval.glo4003.domain.user.limit.Limit;
import ca.ulaval.glo4003.domain.user.limit.TransactionLimitExceededExeption;
import ca.ulaval.glo4003.util.TransactionBuilder;
import ca.ulaval.glo4003.util.TransactionItemBuilder;
import ca.ulaval.glo4003.util.UserBuilder;
import org.assertj.core.api.ThrowableAssert;
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
  private static final String SOME_OTHER_TITLE = "APPL";
  private static final int SOME_OTHER_QTY = 3;

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
  private Limit limit;

  private Transaction transaction;
  private Notification notification;

  private User user;

  @Before
  public void setup() throws StockNotFoundException {
    transaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_QTY).build())
        .withItem(new TransactionItemBuilder().withTitle(SOME_OTHER_TITLE).withQuantity(SOME_OTHER_QTY).build())
        .build();
    notification = new Notification("title", "message");

    given(stockRepository.exists(SOME_TITLE)).willReturn(true);
    given(stockRepository.exists(SOME_OTHER_TITLE)).willReturn(true);
    user = new UserBuilder().withEmail(SOME_EMAIL).withPassword(SOME_PASSWORD).withLimit(limit).build();
    user.getCart().add(SOME_TITLE, SOME_QTY, stockRepository);
    user.getCart().add(SOME_OTHER_TITLE, SOME_OTHER_QTY, stockRepository);

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
  public void whenCheckoutCart_thenReturnCalculatedTransaction() throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption {
    Transaction result = user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);

    assertThat(result).isEqualTo(transaction);
  }

  @Test
  public void whenCheckoutCart_thenPaymentIsProcessedWithTheCurrentTransaction() throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption {
    user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);

    verify(paymentProcessor).payment(transaction);
  }

  @Test
  public void whenCheckoutCart_thenANotificationIsSent() throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption {
    user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);

    verify(notificationSender).sendNotification(eq(notification), any());
  }

  @Test
  public void whenCheckoutCart_thenContentIsAddedToPortfolio() throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption {
    user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);

    assertThat(user.getPortfolio().getStocks().getTitles()).containsExactlyInAnyOrder(SOME_TITLE, SOME_OTHER_TITLE);
    assertThat(user.getPortfolio().getStocks().getQuantity(SOME_TITLE)).isEqualTo(SOME_QTY);
    assertThat(user.getPortfolio().getStocks().getQuantity(SOME_OTHER_TITLE)).isEqualTo(SOME_OTHER_QTY);
  }

  @Test
  public void whenCheckoutCart_thenCartIsCleared() throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption {
    user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);

    assertTrue(user.getCart().isEmpty());
  }

  @Test
  public void givenEmptyCart_whenCheckoutCart_thenExceptionIsThrow() {
    user.getCart().empty();

    ThrowableAssert.ThrowingCallable checkoutCart = () ->
        user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);

    assertThatThrownBy(checkoutCart).isInstanceOf(EmptyCartException.class);
    verify(paymentProcessor, never()).payment(any());
    verify(notificationSender, never()).sendNotification(any(), any());
    assertTrue(user.getPortfolio().getStocks().isEmpty());
  }

  @Test
  public void givenTransactionExceedLimit_whenCheckoutCart_thenExceptionIsThrow() throws TransactionLimitExceededExeption {
    doThrow(TransactionLimitExceededExeption.class).when(limit).checkIfTransactionExceed(transaction);

    ThrowableAssert.ThrowingCallable checkoutCart = () ->
        user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);

    assertThatThrownBy(checkoutCart).isInstanceOf(TransactionLimitExceededExeption.class);
    verify(paymentProcessor, never()).payment(any());
    verify(notificationSender, never()).sendNotification(any(), any());
    assertTrue(user.getPortfolio().getStocks().isEmpty());
  }
}
