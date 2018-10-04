package ca.ulaval.glo4003.service.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.notification.Notification;
import ca.ulaval.glo4003.domain.notification.NotificationFactory;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.transaction.PaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionFactory;
import ca.ulaval.glo4003.domain.transaction.TransactionLedger;
import ca.ulaval.glo4003.domain.user.CurrentUserRepository;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.ws.api.cart.CartItemResponseDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
  private CurrentUserRepository currentUserRepository;
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
  private CartStockItemAssembler cartStockItemAssembler;
  @Mock
  private CartItemResponseDto expectedDto;

  private CheckoutService checkoutService;

  @Before
  public void setup() {
    given(currentUserRepository.getCurrentUser()).willReturn(currentUser);
    given(currentUser.getCart()).willReturn(cart);
    given(transactionFactory.create(cart)).willReturn(transaction);
    given(cart.isEmpty()).willReturn(false);

    checkoutService = new CheckoutService(paymentProcessor,
        currentUserRepository,
        transactionFactory,
        transactionLedger,
        notificationSender,
        notificationFactory,
        cartStockItemAssembler);
  }

  @Test
  public void whenCheckoutCart_thenPaymentIsProceedWithTheCurrentTransaction() {
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

    verify(notificationSender).sendNotification(notification);
  }

  @Test
  public void whenCheckoutCart_thenReturningPreviousCartContent() {
    given(cart.getItems()).willReturn(new ArrayList<>());
    given(cartStockItemAssembler.toDtoList(cart.getItems()))
        .willReturn(Collections.singletonList(expectedDto));

    List<CartItemResponseDto> cartItemResponseDtos = checkoutService.checkoutCart();

    assertThat(cartItemResponseDtos.get(0)).isEqualTo(expectedDto);
  }

  @Test
  public void whenCheckoutCart_thenCartIsCleared() {
    checkoutService.checkoutCart();

    verify(cart).empty();
  }

  @Test
  public void givenEmptyCart_whenCheckoutCart_thenNoProcessIsDone() {
    given(cart.isEmpty()).willReturn(true);

    checkoutService.checkoutCart();

    verify(paymentProcessor, never()).payment(any());
    verify(transactionLedger, never()).save(any());
    verify(notificationSender, never()).sendNotification(any());
  }
}
