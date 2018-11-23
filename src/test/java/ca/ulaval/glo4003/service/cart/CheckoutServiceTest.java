package ca.ulaval.glo4003.service.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import ca.ulaval.glo4003.domain.market.HaltedMarketException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.notification.NotificationFactory;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.transaction.PaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionFactory;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.domain.user.exceptions.EmptyCartException;
import ca.ulaval.glo4003.domain.user.limit.TransactionLimitExceededExeption;
import ca.ulaval.glo4003.service.cart.assemblers.TransactionAssembler;
import ca.ulaval.glo4003.service.cart.dto.TransactionDto;
import ca.ulaval.glo4003.service.cart.exceptions.EmptyCartOnCheckoutException;
import ca.ulaval.glo4003.service.cart.exceptions.InvalidStockTitleException;
import ca.ulaval.glo4003.service.cart.exceptions.PurchaseLimitExceededOnCheckoutException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutServiceTest {
  private static final String SOME_HALTED_MESSAGE = "market halted";
  private static final String SOME_TITLE = "MSFT";

  @Mock
  private TransactionFactory transactionFactory;
  @Mock
  private PaymentProcessor paymentProcessor;
  @Mock
  private NotificationFactory notificationFactory;
  @Mock
  private NotificationSender notificationSender;
  @Mock
  private TransactionAssembler transactionAssembler;
  @Mock
  private StockRepository stockRepository;
  @Mock
  private MarketRepository marketRepository;
  @Mock
  private Investor currentInvestor;
  @Mock
  private Transaction transaction;
  @Mock
  private TransactionDto expectedDto;

  private CheckoutService checkoutService;

  @Before
  public void setup() {
    CurrentUserSession currentUserSession = new CurrentUserSession();
    currentUserSession.setCurrentUser(currentInvestor);
    given(transactionAssembler.toDto(transaction)).willReturn(expectedDto);

    checkoutService = new CheckoutService(
        currentUserSession,
        transactionFactory,
        paymentProcessor,
        notificationFactory,
        notificationSender,
        transactionAssembler,
        stockRepository,
        marketRepository);
  }

  @Test
  public void whenCheckoutCart_thenTransactionIsAssembledToDto()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption,
      HaltedMarketException {
    given(currentInvestor.checkoutCart(transactionFactory, marketRepository, paymentProcessor,
        stockRepository, notificationFactory, notificationSender)).willReturn(transaction);

    TransactionDto transactionDto = checkoutService.checkoutCart();

    assertThat(transactionDto).isEqualTo(expectedDto);
  }

  @Test
  public void whenCheckoutCartThrowingStockNotFound_thenExceptionIsTransformed()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption,
      HaltedMarketException {
    given(currentInvestor.checkoutCart(any(), any(), any(), any(), any(), any())).willThrow(new StockNotFoundException(SOME_TITLE));

    ThrowingCallable checkoutCart = () -> checkoutService.checkoutCart();

    assertThatThrownBy(checkoutCart).isInstanceOf(InvalidStockTitleException.class);
  }

  @Test
  public void whenCheckoutCartThrowingEmptyCart_thenExceptionIsTransformed()
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption,
      HaltedMarketException {
    given(currentInvestor.checkoutCart(any(), any(), any(), any(), any(), any())).willThrow(new EmptyCartException());

    ThrowingCallable checkoutCart = () -> checkoutService.checkoutCart();

    assertThatThrownBy(checkoutCart).isInstanceOf(EmptyCartOnCheckoutException.class);
  }

  @Test
  public void whenCheckoutCartThrowingExceedLimit_thenExceptionIsTransformed()
      throws EmptyCartException, TransactionLimitExceededExeption, StockNotFoundException,
      HaltedMarketException {
    given(currentInvestor.checkoutCart(any(), any(), any(), any(), any(), any())).willThrow(new TransactionLimitExceededExeption());

    ThrowingCallable checkoutCart = () -> checkoutService.checkoutCart();

    assertThatThrownBy(checkoutCart).isInstanceOf(PurchaseLimitExceededOnCheckoutException.class);
  }

  @Test
  public void givenMarketHaltedOnCheckout_whenCheckout_thenExceptionIsTransformed()
      throws HaltedMarketException, StockNotFoundException,
      EmptyCartException, TransactionLimitExceededExeption {
    given(currentInvestor.checkoutCart(any(), any(), any(), any(), any(), any())).willThrow(new HaltedMarketException(SOME_HALTED_MESSAGE));

    assertThatExceptionOfType(HaltedMarketOnCheckoutException.class).isThrownBy(() -> checkoutService.checkoutCart());
  }
}
