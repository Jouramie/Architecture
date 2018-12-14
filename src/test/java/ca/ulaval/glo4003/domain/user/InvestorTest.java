package ca.ulaval.glo4003.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.cart.EmptyCartException;
import ca.ulaval.glo4003.domain.market.MarketBuilder;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.exception.HaltedMarketException;
import ca.ulaval.glo4003.domain.market.exception.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.state.Market;
import ca.ulaval.glo4003.domain.notification.Notification;
import ca.ulaval.glo4003.domain.notification.NotificationFactory;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.stock.exception.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.transaction.PaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionBuilder;
import ca.ulaval.glo4003.domain.transaction.TransactionFactory;
import ca.ulaval.glo4003.domain.transaction.TransactionItemBuilder;
import ca.ulaval.glo4003.domain.user.limit.Limit;
import ca.ulaval.glo4003.domain.user.limit.TransactionLimitExceededExeption;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
  private static final LocalDateTime SOME_DATE = LocalDate.of(2018, 11, 3).atStartOfDay();

  @Mock
  private StockRepository stockRepository;
  @Mock
  private MarketRepository marketRepository;
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
  private Market market;

  @Before
  public void setup() throws StockNotFoundException, MarketNotFoundException {
    transaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_QTY).build())
        .withItem(new TransactionItemBuilder().withTitle(SOME_OTHER_TITLE).withQuantity(SOME_OTHER_QTY).build())
        .build();
    notification = new Notification("title", "message");

    market = new MarketBuilder().build();
    given(marketRepository.findByStock(SOME_TITLE)).willReturn(market);
    given(marketRepository.findByStock(SOME_OTHER_TITLE)).willReturn(market);

    given(stockRepository.exists(SOME_TITLE)).willReturn(true);
    given(stockRepository.exists(SOME_OTHER_TITLE)).willReturn(true);
    investor = new UserBuilder().withLimit(limit).buildInvestor();
    investor.getCart().add(SOME_TITLE, SOME_QTY, stockRepository);
    investor.getCart().add(SOME_OTHER_TITLE, SOME_OTHER_QTY, stockRepository);

    given(transactionFactory.createPurchase(investor.getCart().getStocks())).willReturn(transaction);
    given(notificationFactory.create(transaction)).willReturn(notification);
  }

  @Test
  public void whenCheckoutCart_thenReturnCalculatedTransaction()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption,
      HaltedMarketException {
    Transaction result = investor.checkoutCart(transactionFactory, marketRepository, paymentProcessor, stockRepository,
        notificationFactory, notificationSender);

    assertThat(result).isEqualTo(transaction);
  }

  @Test
  public void whenCheckoutCart_thenPaymentIsProcessedWithTheCurrentTransaction()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption,
      HaltedMarketException {
    investor.checkoutCart(transactionFactory, marketRepository, paymentProcessor, stockRepository, notificationFactory,
        notificationSender);

    verify(paymentProcessor).payment(transaction);
  }

  @Test
  public void whenCheckoutCart_thenANotificationIsSent()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption,
      HaltedMarketException {
    investor.checkoutCart(transactionFactory, marketRepository, paymentProcessor, stockRepository, notificationFactory,
        notificationSender);

    verify(notificationSender).sendNotification(eq(notification), any());
  }

  @Test
  public void whenCheckoutCart_thenContentIsAddedToPortfolio()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption,
      HaltedMarketException {
    investor.checkoutCart(transactionFactory, marketRepository, paymentProcessor, stockRepository, notificationFactory,
        notificationSender);

    assertThat(investor.getPortfolio().getStocks().getTitles()).containsExactlyInAnyOrder(SOME_TITLE, SOME_OTHER_TITLE);
    assertThat(investor.getPortfolio().getStocks().getQuantity(SOME_TITLE)).isEqualTo(SOME_QTY);
    assertThat(investor.getPortfolio().getStocks().getQuantity(SOME_OTHER_TITLE)).isEqualTo(SOME_OTHER_QTY);
  }

  @Test
  public void whenCheckoutCart_thenCartIsCleared()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption,
      HaltedMarketException {
    investor.checkoutCart(transactionFactory, marketRepository, paymentProcessor, stockRepository, notificationFactory,
        notificationSender);

    assertThat(investor.getCart().isEmpty()).isTrue();
  }

  @Test
  public void givenEmptyCart_whenCheckoutCart_thenExceptionIsThrow() {
    investor.getCart().empty();

    ThrowingCallable checkoutCart = () -> investor.checkoutCart(transactionFactory, marketRepository, paymentProcessor,
        stockRepository, notificationFactory, notificationSender);

    assertThatThrownBy(checkoutCart).isInstanceOf(EmptyCartException.class);
    verify(paymentProcessor, never()).payment(any());
    verify(notificationSender, never()).sendNotification(any(), any());
    assertThat(investor.getPortfolio().getStocks().isEmpty()).isTrue();
  }

  @Test
  public void givenTransactionExceedLimit_whenCheckoutCart_thenExceptionIsThrow() throws TransactionLimitExceededExeption {
    doThrow(TransactionLimitExceededExeption.class).when(limit).ensureTransactionIsUnderLimit(transaction);

    ThrowingCallable checkoutCart = () -> investor.checkoutCart(transactionFactory, marketRepository, paymentProcessor,
        stockRepository, notificationFactory, notificationSender);

    assertThatThrownBy(checkoutCart).isInstanceOf(TransactionLimitExceededExeption.class);
    verify(paymentProcessor, never()).payment(any());
    verify(notificationSender, never()).sendNotification(any(), any());
    assertThat(investor.getCart().getStocks().isEmpty()).isFalse();
    assertThat(investor.getPortfolio().getStocks().isEmpty()).isTrue();
  }

  @Test
  public void givenMarketHalted_whenCheckoutCart_thenExceptionIsPropagated()
      throws StockNotFoundException, EmptyCartException, HaltedMarketException {
    Cart cart = mock(Cart.class);
    given(cart.checkout(transactionFactory, marketRepository)).willThrow(new HaltedMarketException(SOME_HALTED_MESSAGE));
    investor = new UserBuilder().withCart(cart).buildInvestor();

    ThrowingCallable checkout = () -> investor.checkoutCart(transactionFactory, marketRepository, paymentProcessor,
        stockRepository, notificationFactory, notificationSender);

    assertThatThrownBy(checkout).isInstanceOf(HaltedMarketException.class);
  }

  @Test
  public void whenGetTransactions_thenTransactionsAreGottenFromPortfolio() {
    LocalDateTime from = SOME_DATE.minusDays(1);
    LocalDateTime to = SOME_DATE;
    Portfolio portfolio = mock(Portfolio.class);
    Investor investor = new UserBuilder().withPortfolio(portfolio).buildInvestor();

    investor.getTransactions(from, to);

    verify(portfolio).getTransactions(from, to);
  }
}
