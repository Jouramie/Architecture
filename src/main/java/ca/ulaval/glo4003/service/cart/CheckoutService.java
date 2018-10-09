package ca.ulaval.glo4003.service.cart;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.notification.Notification;
import ca.ulaval.glo4003.domain.notification.NotificationFactory;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.transaction.PaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionFactory;
import ca.ulaval.glo4003.domain.transaction.TransactionLedger;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.infrastructure.injection.Component;
import ca.ulaval.glo4003.ws.api.cart.CartItemResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import javax.inject.Inject;

@Component
public class CheckoutService {
  private final PaymentProcessor paymentProcessor;
  private final CurrentUserSession currentUserSession;
  private final TransactionFactory transactionFactory;
  private final TransactionLedger transactionLedger;
  private final NotificationSender notificationSender;
  private final NotificationFactory notificationFactory;
  private final CartItemAssembler cartItemAssembler;

  @Inject
  public CheckoutService(PaymentProcessor paymentProcessor,
                         CurrentUserSession currentUserSession,
                         TransactionFactory transactionFactory,
                         TransactionLedger transactionLedger,
                         NotificationSender notificationSender,
                         NotificationFactory notificationFactory,
                         CartItemAssembler cartItemAssembler) {

    this.paymentProcessor = paymentProcessor;
    this.currentUserSession = currentUserSession;
    this.transactionFactory = transactionFactory;
    this.transactionLedger = transactionLedger;
    this.notificationSender = notificationSender;
    this.notificationFactory = notificationFactory;
    this.cartItemAssembler = cartItemAssembler;
  }

  public List<CartItemResponseDto> checkoutCart() {
    User currentUser = currentUserSession.getCurrentUser();
    Cart cart = currentUser.getCart();
    checkIfCartIsEmpty(cart);

    Transaction transaction = createTransaction(cart);
    processTransaction(transaction);
    sendTransactionNotification(transaction, currentUser);

    List<CartItemResponseDto> cartItemResponseDtos = assembleResponse(cart);
    cart.empty();
    return cartItemResponseDtos;
  }

  private void checkIfCartIsEmpty(Cart cart) {
    if (cart.isEmpty()) {
      throw new EmptyCartException();
    }
  }

  private Transaction createTransaction(Cart cart) {
    try {
      return transactionFactory.createPurchase(cart);
    } catch (StockNotFoundException exception) {
      throw new InvalidStockTitleException(exception.title);
    }
  }

  private List<CartItemResponseDto> assembleResponse(Cart cart) {
    try {
      return cartItemAssembler.toDtoList(cart.getItems());
    } catch (StockNotFoundException exception) {
      throw new InvalidStockTitleException(exception.title);
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
