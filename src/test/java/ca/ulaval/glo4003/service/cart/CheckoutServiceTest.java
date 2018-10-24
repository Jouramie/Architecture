package ca.ulaval.glo4003.service.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.notification.Notification;
import ca.ulaval.glo4003.domain.notification.NotificationFactory;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.transaction.PaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionFactory;
import ca.ulaval.glo4003.domain.transaction.TransactionLedger;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.ws.api.cart.TransactionDto;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutServiceTest {
  @Mock
  private PaymentProcessor paymentProcessor;
  @Mock
  private CurrentUserSession currentUserSession;
  @Mock
  private User currentUser;
  @Mock
  private Cart cart;
  @Mock
  private TransactionFactory transactionFactory;
  @Mock
  private Transaction transaction;
  @Mock
  private TransactionLedger transactionLedger;
  @Mock
  private NotificationSender notificationSender;
  @Mock
  private NotificationFactory notificationFactory;
  @Mock
  private Notification notification;
  @Mock
  private TransactionAssembler transactionAssembler;
  @Mock
  private TransactionDto expectedDto;
  @Mock
  private StockCollection stockCollection;
  @Mock
  private StockRepository stockRepository;

  private CheckoutService checkoutService;

  @Before
  public void setup() throws StockNotFoundException {
    given(currentUserSession.getCurrentUser()).willReturn(currentUser);
    given(currentUser.getCart()).willReturn(cart);
    given(transactionFactory.createPurchase(cart)).willReturn(transaction);
    given(cart.isEmpty()).willReturn(false);
    given(cart.getStocks()).willReturn(stockCollection);
    given(stockCollection.getTitles()).willReturn(Arrays.asList("stock1", "stock2", "stock3"));

    checkoutService = new CheckoutService(paymentProcessor,
        currentUserSession,
        transactionFactory,
        transactionLedger,
        notificationSender,
        notificationFactory,
        transactionAssembler,
        stockRepository);
  }

  @Test
  public void whenCheckoutCart_thenPaymentIsProcessedWithTheCurrentTransaction() {
    checkoutService.checkoutCart();

    verify(paymentProcessor).payment(transaction);
  }

  @Test
  public void whenCheckoutCart_thenTransactionIsAddedToTheLedger() {
    checkoutService.checkoutCart();

    verify(transactionLedger).save(transaction);
  }

  @Test
  public void whenCheckoutCart_thenANotificationIsSend() {
    given(notificationFactory.create(transaction)).willReturn(notification);

    checkoutService.checkoutCart();

    verify(notificationSender).sendNotification(notification, currentUser);
  }

  @Test
  public void whenCheckoutCart_thenStocksAreAddedToUserPortfolio() {
    checkoutService.checkoutCart();

    int numberOfStocksInCart = cart.getStocks().getTitles().size();
    verify(currentUser, times(numberOfStocksInCart)).addStockToPortfolio(any(), anyInt(), any());
  }

  @Test
  public void whenCheckoutCart_thenPreviousCartContentIsReturned() {
    given(transactionAssembler.toDto(transaction))
        .willReturn(expectedDto);

    TransactionDto apiTransactionDto = checkoutService.checkoutCart();

    assertThat(apiTransactionDto).isEqualTo(expectedDto);
  }

  @Test
  public void whenCheckoutCart_thenCartIsCleared() {
    checkoutService.checkoutCart();

    verify(cart).empty();
  }

  @Test
  public void givenEmptyCart_whenCheckoutCart_thenThrowCheckoutEmptyCartException() {
    given(cart.isEmpty()).willReturn(true);

    assertThatThrownBy(() -> checkoutService.checkoutCart()).isInstanceOf(EmptyCartException.class);

    verify(paymentProcessor, never()).payment(any());
    verify(transactionLedger, never()).save(any());
    verify(notificationSender, never()).sendNotification(any(), any());
  }

  @Test
  public void givenOneStockOfCartDoesNotExist_whenCheckoutCart_thenInvalidStockTitleExceptionIsThrown()
      throws StockNotFoundException {
    doThrow(StockNotFoundException.class).when(transactionFactory).createPurchase(any());

    assertThatThrownBy(() -> checkoutService.checkoutCart())
        .isInstanceOf(InvalidStockTitleException.class);
  }
}
