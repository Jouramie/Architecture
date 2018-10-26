package ca.ulaval.glo4003.service.cart;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.notification.Notification;
import ca.ulaval.glo4003.domain.notification.NotificationFactory;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.transaction.PaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionFactory;
import ca.ulaval.glo4003.domain.transaction.TransactionLedger;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.service.Component;
import javax.inject.Inject;

@Component
public class CheckoutService {
  private final PaymentProcessor paymentProcessor;
  private final CurrentUserSession currentUserSession;
  private final TransactionFactory transactionFactory;
  private final TransactionLedger transactionLedger;
  private final NotificationSender notificationSender;
  private final NotificationFactory notificationFactory;
  private final TransactionAssembler transactionAssembler;
  private final StockRepository stockRepository;

  @Inject
  public CheckoutService(PaymentProcessor paymentProcessor,
                         CurrentUserSession currentUserSession,
                         TransactionFactory transactionFactory,
                         TransactionLedger transactionLedger,
                         NotificationSender notificationSender,
                         NotificationFactory notificationFactory,
                         TransactionAssembler transactionAssembler,
                         StockRepository stockRepository) {

    this.paymentProcessor = paymentProcessor;
    this.currentUserSession = currentUserSession;
    this.transactionFactory = transactionFactory;
    this.transactionLedger = transactionLedger;
    this.notificationSender = notificationSender;
    this.notificationFactory = notificationFactory;
    this.transactionAssembler = transactionAssembler;
    this.stockRepository = stockRepository;
  }

  public TransactionDto checkoutCart() throws InvalidStockTitleException {
    User currentUser = currentUserSession.getCurrentUser();
    Cart cart = currentUser.getCart();
    checkIfCartIsEmpty(cart);

    Transaction transaction = createTransaction(cart);
    processTransaction(transaction);
    sendTransactionNotification(transaction, currentUser);
    addStocksToUserPortfolio(currentUser, cart);
    cart.empty();
    return transactionAssembler.toDto(transaction);
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
      throw new InvalidStockTitleException(exception);
    }
  }

  private void sendTransactionNotification(Transaction transaction, User currentUser) {
    Notification notification = notificationFactory.create(transaction);
    notificationSender.sendNotification(notification, currentUser);
  }

  private void addStocksToUserPortfolio(User currentUser, Cart cart) throws InvalidStockTitleException {
    for (String title : cart.getStocks().getTitles()) {
      currentUser.addStockToPortfolio(title, cart.getQuantity(title), stockRepository);
    }
  }

  private void processTransaction(Transaction transaction) {
    paymentProcessor.payment(transaction);
    transactionLedger.save(transaction);
  }
}
