package ca.ulaval.glo4003.service.cart;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.notification.Notification;
import ca.ulaval.glo4003.domain.notification.NotificationFactory;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.transaction.PaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionFactory;
import ca.ulaval.glo4003.domain.transaction.TransactionLedger;
import ca.ulaval.glo4003.domain.user.CurrentUserRepository;
import ca.ulaval.glo4003.ws.api.cart.CartItemResponseDto;
import java.util.ArrayList;
import java.util.List;

public class CheckoutService {
  private final PaymentProcessor paymentProcessor;
  private final CurrentUserRepository currentUserRepository;
  private final TransactionFactory transactionFactory;
  private final TransactionLedger transactionLedger;
  private final NotificationSender notificationSender;
  private final NotificationFactory notificationFactory;
  private final CartStockItemAssembler cartStockItemAssembler;

  public CheckoutService(PaymentProcessor paymentProcessor,
                         CurrentUserRepository currentUserRepository,
                         TransactionFactory transactionFactory,
                         TransactionLedger transactionLedger,
                         NotificationSender notificationSender,
                         NotificationFactory notificationFactory,
                         CartStockItemAssembler cartStockItemAssembler) {

    this.paymentProcessor = paymentProcessor;
    this.currentUserRepository = currentUserRepository;
    this.transactionFactory = transactionFactory;
    this.transactionLedger = transactionLedger;
    this.notificationSender = notificationSender;
    this.notificationFactory = notificationFactory;
    this.cartStockItemAssembler = cartStockItemAssembler;
  }

  public List<CartItemResponseDto> checkoutCart() {
    Cart cart = currentUserRepository.getCurrentUser().getCart();
    if (cart.isEmpty()) {
      return new ArrayList<>();
    }

    Transaction transaction = transactionFactory.create(cart);
    processTransaction(transaction);
    sendTransactionNotification(transaction);

    List<CartItemResponseDto> cartItemResponseDtos = cartStockItemAssembler
        .toDtoList(cart.getItems());
    cart.empty();
    return cartItemResponseDtos;
  }

  private void sendTransactionNotification(Transaction transaction) {
    Notification notification = notificationFactory.create(transaction);
    notificationSender.sendNotification(notification);
  }

  private void processTransaction(Transaction transaction) {
    paymentProcessor.payment(transaction);
    transactionLedger.save(transaction);
  }
}
