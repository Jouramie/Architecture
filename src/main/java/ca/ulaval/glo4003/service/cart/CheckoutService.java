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
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.ws.api.cart.CartItemResponseDto;
import java.util.List;
import javax.inject.Inject;

public class CheckoutService {
  private final PaymentProcessor paymentProcessor;
  private final CurrentUserRepository currentUserRepository;
  private final TransactionFactory transactionFactory;
  private final TransactionLedger transactionLedger;
  private final NotificationSender notificationSender;
  private final NotificationFactory notificationFactory;
  private final CartItemAssembler cartItemAssembler;

  @Inject
  public CheckoutService(PaymentProcessor paymentProcessor,
                         CurrentUserRepository currentUserRepository,
                         TransactionFactory transactionFactory,
                         TransactionLedger transactionLedger,
                         NotificationSender notificationSender,
                         NotificationFactory notificationFactory,
                         CartItemAssembler cartItemAssembler) {

    this.paymentProcessor = paymentProcessor;
    this.currentUserRepository = currentUserRepository;
    this.transactionFactory = transactionFactory;
    this.transactionLedger = transactionLedger;
    this.notificationSender = notificationSender;
    this.notificationFactory = notificationFactory;
    this.cartItemAssembler = cartItemAssembler;
  }

  public List<CartItemResponseDto> checkoutCart() {
    User currentUser = currentUserRepository.getCurrentUser();
    Cart cart = currentUser.getCart();
    checkIfCartIsEmpty(cart);

    Transaction transaction = transactionFactory.createPurchase(cart);
    processTransaction(transaction);
    sendTransactionNotification(transaction, currentUser);

    List<CartItemResponseDto> cartItemResponseDtos = cartItemAssembler
        .toDtoList(cart.getItems());
    cart.empty();
    return cartItemResponseDtos;
  }

  private void checkIfCartIsEmpty(Cart cart) {
    if (cart.isEmpty()) {
      throw new CheckoutEmptyCartException();
    }
  }

  private void sendTransactionNotification(Transaction transaction, User currentUser) {
    Notification notification = notificationFactory.create(transaction);
    notificationSender.sendNotification(notification, currentUser);
  }

  private void processTransaction(Transaction transaction) {
    paymentProcessor.payment(transaction);
    transactionLedger.save(transaction);
  }
}
