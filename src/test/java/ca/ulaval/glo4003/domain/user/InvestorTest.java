package ca.ulaval.glo4003.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InvestorTest {

  private static final String SOME_TITLE = "MSFT";
  private static final int SOME_QTY = 2;
  private static final String SOME_OTHER_TITLE = "APPL";
  private static final int SOME_OTHER_QTY = 3;
  private static final String SOME_HALTED_MESSAGE = "STOP";

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

  private Investor investor;

  @Before
  public void setup() throws StockNotFoundException, HaltedMarketException {
    transaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_QTY).build())
        .withItem(new TransactionItemBuilder().withTitle(SOME_OTHER_TITLE).withQuantity(SOME_OTHER_QTY).build())
        .build();
    notification = new Notification("title", "message");

    given(stockRepository.exists(SOME_TITLE)).willReturn(true);
    given(stockRepository.exists(SOME_OTHER_TITLE)).willReturn(true);
    investor = new UserBuilder().withLimit(limit).buildInvestor();
    investor.getCart().add(SOME_TITLE, SOME_QTY, stockRepository);
    investor.getCart().add(SOME_OTHER_TITLE, SOME_OTHER_QTY, stockRepository);

    given(transactionFactory.createPurchase(investor.getCart())).willReturn(transaction);
    given(notificationFactory.create(transaction)).willReturn(notification);
  }

  @Test
  public void whenGetRole_thenRoleIsInvestor() {
    UserRole role = investor.getRole();

    assertThat(role).isSameAs(UserRole.INVESTOR);
  }

  @Test
  public void whenCheckoutCart_thenReturnCalculatedTransaction()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption, HaltedMarketException {
    Transaction result = investor.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);

    assertThat(result).isEqualTo(transaction);
  }

  @Test
  public void whenCheckoutCart_thenPaymentIsProcessedWithTheCurrentTransaction()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption, HaltedMarketException {
    investor.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);

    verify(paymentProcessor).payment(transaction);
  }

  @Test
  public void whenCheckoutCart_thenANotificationIsSent()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption, HaltedMarketException {
    investor.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);

    verify(notificationSender).sendNotification(eq(notification), any());
  }

  @Test
  public void whenCheckoutCart_thenContentIsAddedToPortfolio()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption, HaltedMarketException {
    investor.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender,
        stockRepository);

    assertThat(investor.getPortfolio().getStocks().getTitles()).containsExactlyInAnyOrder(SOME_TITLE, SOME_OTHER_TITLE);
    assertThat(investor.getPortfolio().getStocks().getQuantity(SOME_TITLE)).isEqualTo(SOME_QTY);
    assertThat(investor.getPortfolio().getStocks().getQuantity(SOME_OTHER_TITLE)).isEqualTo(SOME_OTHER_QTY);
  }

  @Test
  public void whenCheckoutCart_thenCartIsCleared()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption, HaltedMarketException {
    investor.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender,
        stockRepository);

    assertThat(investor.getCart().isEmpty()).isTrue();
  }

  @Test
  public void givenEmptyCart_whenCheckoutCart_thenExceptionIsThrow() {
    investor.getCart().empty();

    ThrowingCallable checkoutCart = () ->
        investor.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender,
            stockRepository);

    assertThatThrownBy(checkoutCart).isInstanceOf(EmptyCartException.class);
    verify(paymentProcessor, never()).payment(any());
    verify(notificationSender, never()).sendNotification(any(), any());
    assertThat(investor.getPortfolio().getStocks().isEmpty()).isTrue();
  }

  @Test
  public void givenTransactionExceedLimit_whenCheckoutCart_thenExceptionIsThrow()
      throws TransactionLimitExceededExeption {
    doThrow(TransactionLimitExceededExeption.class).when(limit).checkIfTransactionExceed(transaction);

    ThrowingCallable checkoutCart = () ->
        investor.checkoutCart(transactionFactory, paymentProcessor, notificationFactory, notificationSender,
            stockRepository);

    assertThatThrownBy(checkoutCart).isInstanceOf(TransactionLimitExceededExeption.class);
    verify(paymentProcessor, never()).payment(any());
    verify(notificationSender, never()).sendNotification(any(), any());
    assertThat(investor.getPortfolio().getStocks().isEmpty()).isTrue();
  }

  @Test
  public void givenMarketHalted_whenCheckoutCart_thenExceptionIsPropagated()
      throws HaltedMarketException, StockNotFoundException {
    given(transactionFactory.createPurchase(any())).willThrow(new HaltedMarketException(SOME_HALTED_MESSAGE));

    ThrowingCallable checkout = () -> investor.checkoutCart(transactionFactory, paymentProcessor, notificationFactory,
        notificationSender, stockRepository);

    assertThatExceptionOfType(HaltedMarketException.class)
        .isThrownBy(checkout);
  }
}
