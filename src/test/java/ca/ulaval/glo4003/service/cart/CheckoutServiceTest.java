package ca.ulaval.glo4003.service.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import ca.ulaval.glo4003.domain.notification.NotificationFactory;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.transaction.PaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionFactory;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.exceptions.EmptyCartException;
import ca.ulaval.glo4003.domain.user.limit.TransactionExceedLimitException;
import ca.ulaval.glo4003.service.cart.assemblers.TransactionAssembler;
import ca.ulaval.glo4003.service.cart.dto.TransactionDto;
import ca.ulaval.glo4003.service.cart.exceptions.EmptyCartOnCheckoutException;
import ca.ulaval.glo4003.service.cart.exceptions.InvalidStockTitleException;
import ca.ulaval.glo4003.service.cart.exceptions.PurchaseExceedUserLimitException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutServiceTest {
  private static final String SOME_TITLE = "MSFT";

  @Mock
  private CurrentUserSession currentUserSession;
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
  private User currentUser;
  @Mock
  private Transaction transaction;
  @Mock
  private TransactionDto expectedDto;

  private CheckoutService checkoutService;

  @Before
  public void setup() {
    given(currentUserSession.getCurrentUser()).willReturn(currentUser);
    given(transactionAssembler.toDto(transaction)).willReturn(expectedDto);

    checkoutService = new CheckoutService(
        currentUserSession,
        transactionFactory,
        paymentProcessor,
        notificationFactory,
        notificationSender,
        transactionAssembler,
        stockRepository);
  }

  @Test
  public void whenCheckoutCart_thenTransactionIsAssembledToDto()
      throws StockNotFoundException, EmptyCartException, TransactionExceedLimitException {
    given(currentUser.checkoutCart(transactionFactory, paymentProcessor,
        notificationFactory, notificationSender, stockRepository)).willReturn(transaction);

    TransactionDto transactionDto = checkoutService.checkoutCart();

    assertThat(transactionDto).isEqualTo(expectedDto);
  }

  @Test
  public void whenCheckoutCartThrowingStockNotFound_thenExceptionIsTransformed()
      throws StockNotFoundException, EmptyCartException, TransactionExceedLimitException {
    given(currentUser.checkoutCart(any(), any(), any(), any(), any())).willThrow(new StockNotFoundException(SOME_TITLE));

    assertThatThrownBy(() -> checkoutService.checkoutCart()).isInstanceOf(InvalidStockTitleException.class);
  }

  @Test
  public void whenCheckoutCartThrowingEmptyCart_thenExceptionIsTransformed()
      throws StockNotFoundException, EmptyCartException, TransactionExceedLimitException {
    given(currentUser.checkoutCart(any(), any(), any(), any(), any())).willThrow(new EmptyCartException());

    assertThatThrownBy(() -> checkoutService.checkoutCart()).isInstanceOf(EmptyCartOnCheckoutException.class);
  }

  @Test
  public void whenCheckoutCartThrowingExceedLimit_thenExceptionIsTransformed()
      throws EmptyCartException, TransactionExceedLimitException, StockNotFoundException {
    given(currentUser.checkoutCart(any(), any(), any(), any(), any())).willThrow(new TransactionExceedLimitException());

    assertThatThrownBy(() -> checkoutService.checkoutCart()).isInstanceOf(PurchaseExceedUserLimitException.class);
  }
}
