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
    this.transaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_QTY).build())
        .withItem(new TransactionItemBuilder().withTitle(SOME_OTHER_TITLE).withQuantity(SOME_OTHER_QTY).build())
        .build();
    this.notification = new Notification("title", "message");

    given(this.stockRepository.exists(SOME_TITLE)).willReturn(true);
    given(this.stockRepository.exists(SOME_OTHER_TITLE)).willReturn(true);
    this.user = new UserBuilder().withEmail(SOME_EMAIL).withPassword(SOME_PASSWORD).withLimit(this.limit).build();
    this.user.getCart().add(SOME_TITLE, SOME_QTY, this.stockRepository);
    this.user.getCart().add(SOME_OTHER_TITLE, SOME_OTHER_QTY, this.stockRepository);

    given(this.transactionFactory.createPurchase(this.user.getCart())).willReturn(this.transaction);
    given(this.notificationFactory.create(this.transaction)).willReturn(this.notification);
  }

  @Test
  public void givenRightPassword_whenCheckingIfPasswordBelongsToUser_thenItDoes() {
    assertThat(this.user.isThisYourPassword(SOME_PASSWORD)).isTrue();
  }

  @Test
  public void givenWrongPassword_whenCheckingIfPasswordBelongsToUser_thenItDoesNot() {
    assertThat(this.user.isThisYourPassword(WRONG_PASSWORD)).isFalse();
  }

  @Test
  public void whenCheckoutCart_thenReturnCalculatedTransaction() throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption {
    Transaction result = this.user.checkoutCart(this.transactionFactory, this.paymentProcessor, this.notificationFactory, this.notificationSender, this.stockRepository);

    assertThat(result).isEqualTo(this.transaction);
  }

  @Test
  public void whenCheckoutCart_thenPaymentIsProcessedWithTheCurrentTransaction() throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption {
    this.user.checkoutCart(this.transactionFactory, this.paymentProcessor, this.notificationFactory, this.notificationSender, this.stockRepository);

    verify(this.paymentProcessor).payment(this.transaction);
  }

  @Test
  public void whenCheckoutCart_thenANotificationIsSent() throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption {
    this.user.checkoutCart(this.transactionFactory, this.paymentProcessor, this.notificationFactory, this.notificationSender, this.stockRepository);

    verify(this.notificationSender).sendNotification(eq(this.notification), any());
  }

  @Test
  public void whenCheckoutCart_thenContentIsAddedToPortfolio() throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption {
    this.user.checkoutCart(this.transactionFactory, this.paymentProcessor, this.notificationFactory, this.notificationSender, this.stockRepository);

    assertThat(this.user.getPortfolio().getStocks().getTitles()).containsExactlyInAnyOrder(SOME_TITLE, SOME_OTHER_TITLE);
    assertThat(this.user.getPortfolio().getStocks().getQuantity(SOME_TITLE)).isEqualTo(SOME_QTY);
    assertThat(this.user.getPortfolio().getStocks().getQuantity(SOME_OTHER_TITLE)).isEqualTo(SOME_OTHER_QTY);
  }

  @Test
  public void whenCheckoutCart_thenCartIsCleared() throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption {
    this.user.checkoutCart(this.transactionFactory, this.paymentProcessor, this.notificationFactory, this.notificationSender, this.stockRepository);

    assertTrue(this.user.getCart().isEmpty());
  }

  @Test
  public void givenEmptyCart_whenCheckoutCart_thenExceptionIsThrow() {
    this.user.getCart().empty();

    ThrowableAssert.ThrowingCallable checkoutCart = () ->
        this.user.checkoutCart(this.transactionFactory, this.paymentProcessor, this.notificationFactory, this.notificationSender, this.stockRepository);

    assertThatThrownBy(checkoutCart).isInstanceOf(EmptyCartException.class);
    verify(this.paymentProcessor, never()).payment(any());
    verify(this.notificationSender, never()).sendNotification(any(), any());
    assertTrue(this.user.getPortfolio().getStocks().isEmpty());
  }

  @Test
  public void givenTransactionExceedLimit_whenCheckoutCart_thenExceptionIsThrow() throws TransactionLimitExceededExeption {
    doThrow(TransactionLimitExceededExeption.class).when(this.limit).checkIfTransactionExceed(this.transaction);

    ThrowableAssert.ThrowingCallable checkoutCart = () ->
        this.user.checkoutCart(this.transactionFactory, this.paymentProcessor, this.notificationFactory, this.notificationSender, this.stockRepository);

    assertThatThrownBy(checkoutCart).isInstanceOf(TransactionLimitExceededExeption.class);
    verify(this.paymentProcessor, never()).payment(any());
    verify(this.notificationSender, never()).sendNotification(any(), any());
    assertTrue(this.user.getPortfolio().getStocks().isEmpty());
  }
}
