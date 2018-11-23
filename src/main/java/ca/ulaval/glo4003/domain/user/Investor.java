package ca.ulaval.glo4003.domain.user;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.notification.Notification;
import ca.ulaval.glo4003.domain.notification.NotificationCoordinates;
import ca.ulaval.glo4003.domain.notification.NotificationFactory;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.transaction.PaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionFactory;
import ca.ulaval.glo4003.domain.user.exceptions.EmptyCartException;
import ca.ulaval.glo4003.domain.user.limit.Limit;
import ca.ulaval.glo4003.domain.user.limit.TransactionLimitExceededExeption;

public class Investor extends User {
  private final Cart cart;
  private final Portfolio portfolio;
  private Limit limit;

  public Investor(String email, String password, Cart cart, Portfolio portfolio, Limit limit) {
    super(email, password);
    this.cart = cart;
    this.portfolio = portfolio;
    this.limit = limit;
  }

  @Override
  public UserRole getRole() {
    return UserRole.INVESTOR;
  }

  public Limit getLimit() {
    return limit;
  }

  public void setLimit(Limit limit) {
    this.limit = limit;
  }

  public Cart getCart() {
    return cart;
  }

  public Portfolio getPortfolio() {
    return portfolio;
  }

  public Transaction checkoutCart(TransactionFactory transactionFactory,
                                  PaymentProcessor paymentProcessor,
                                  NotificationFactory notificationFactory,
                                  NotificationSender notificationSender,
                                  StockRepository stockRepository)
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption {

    checkIfCartIsEmpty(cart);

    Transaction purchase = transactionFactory.createPurchase(cart);
    limit.checkIfTransactionExceed(purchase);

    processPurchase(purchase, paymentProcessor, stockRepository);
    sendTransactionNotification(notificationFactory, notificationSender, purchase);

    cart.empty();

    return purchase;
  }

  private void checkIfCartIsEmpty(Cart cart) throws EmptyCartException {
    if (cart.isEmpty()) {
      throw new EmptyCartException();
    }
  }

  private void processPurchase(Transaction transaction,
                               PaymentProcessor paymentProcessor,
                               StockRepository stockRepository) {
    paymentProcessor.payment(transaction);
    portfolio.add(transaction, stockRepository);
  }

  private void sendTransactionNotification(NotificationFactory notificationFactory,
                                           NotificationSender notificationSender,
                                           Transaction transaction) {
    Notification notification = notificationFactory.create(transaction);
    notificationSender.sendNotification(notification, new NotificationCoordinates(email));
  }
}
