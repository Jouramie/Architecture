package ca.ulaval.glo4003.domain.user;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.market.HaltedMarketException;
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
import java.util.List;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {

  private static final String SOME_EMAIL = "4email@email.com";
  private static final String SOME_PASSWORD = "a password";
  private static final UserRole SOME_ROLE = UserRole.INVESTOR;
  private static final String WRONG_PASSWORD = SOME_PASSWORD + "wrong";
  private static final String SOME_TITLE = "MSFT";
  private static final int SOME_QTY = 2;
  private static final String SOME_OTHER_TITLE = "APPL";
  private static final int SOME_OTHER_QTY = 3;
  private static final String SOME_HALTED_MESSAGE = "market halted";

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
  public void setup() throws StockNotFoundException, HaltedMarketException {
    transaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_QTY).build())
        .withItem(new TransactionItemBuilder().withTitle(SOME_OTHER_TITLE).withQuantity(SOME_OTHER_QTY).build())
        .build();
    notification = new Notification("title", "message");

    given(stockRepository.exists(SOME_TITLE)).willReturn(true);
    given(stockRepository.exists(SOME_OTHER_TITLE)).willReturn(true);
    user = new UserBuilder().withEmail(SOME_EMAIL).withPassword(SOME_PASSWORD).withLimit(limit).withRole(SOME_ROLE).build();
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
  public void whenCheckoutCart_thenReturnCalculatedTransaction()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption,
      HaltedMarketException {
    Transaction result = user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);

    assertThat(result).isEqualTo(transaction);
  }

  @Test
  public void whenCheckoutCart_thenPaymentIsProcessedWithTheCurrentTransaction()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption,
      HaltedMarketException {
    user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);

    verify(paymentProcessor).payment(transaction);
  }

  @Test
  public void whenCheckoutCart_thenANotificationIsSent()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption,
      HaltedMarketException {
    user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);

    verify(notificationSender).sendNotification(eq(notification), any());
  }

  @Test
  public void whenCheckoutCart_thenContentIsAddedToPortfolio()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption,
      HaltedMarketException {
    user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);

    assertThat(user.getPortfolio().getStocks().getTitles()).containsExactlyInAnyOrder(SOME_TITLE, SOME_OTHER_TITLE);
    assertThat(user.getPortfolio().getStocks().getQuantity(SOME_TITLE)).isEqualTo(SOME_QTY);
    assertThat(user.getPortfolio().getStocks().getQuantity(SOME_OTHER_TITLE)).isEqualTo(SOME_OTHER_QTY);
  }

  @Test
  public void whenCheckoutCart_thenCartIsCleared()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption,
      HaltedMarketException {
    user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);

    assertThat(user.getCart().isEmpty()).isTrue();
  }

  @Test
  public void givenEmptyCart_whenCheckoutCart_thenExceptionIsThrow() {
    user.getCart().empty();

    ThrowingCallable checkoutCart = () ->
        user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);

    assertThatThrownBy(checkoutCart).isInstanceOf(EmptyCartException.class);
    verify(paymentProcessor, never()).payment(any());
    verify(notificationSender, never()).sendNotification(any(), any());
    assertThat(user.getPortfolio().getStocks().isEmpty()).isTrue();
  }

  @Test
  public void givenTransactionExceedLimit_whenCheckoutCart_thenExceptionIsThrow() throws TransactionLimitExceededExeption {
    doThrow(TransactionLimitExceededExeption.class).when(limit).checkIfTransactionExceed(transaction);

    ThrowingCallable checkoutCart = () ->
        user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);

    assertThatThrownBy(checkoutCart).isInstanceOf(TransactionLimitExceededExeption.class);
    verify(paymentProcessor, never()).payment(any());
    verify(notificationSender, never()).sendNotification(any(), any());
    assertThat(user.getPortfolio().getStocks().isEmpty()).isTrue();
  }

  @Test
  public void givenListContainingUserRole_whenHaveRoleIn_thenTrue() {
    List<UserRole> roles = asList(UserRole.INVESTOR, UserRole.ADMINISTRATOR);

    boolean haveRoleIn = user.haveRoleIn(roles);

    assertThat(haveRoleIn).isTrue();
  }

  @Test
  public void givenListNotContainingUserRole_whenHaveRoleIn_thenFalse() {
    List<UserRole> roles = emptyList();

    boolean haveRoleIn = user.haveRoleIn(roles);

    assertThat(haveRoleIn).isFalse();
  }

  @Test
  public void givenMarketHalted_whenCheckoutCart_thenExceptionIsPropagated()
      throws HaltedMarketException, StockNotFoundException {
    given(transactionFactory.createPurchase(any(Cart.class))).willThrow(new HaltedMarketException(SOME_HALTED_MESSAGE));

    assertThatExceptionOfType(HaltedMarketException.class)
        .isThrownBy(() ->
            user.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository));
  }
}
