package ca.ulaval.glo4003.domain.user;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.market.HaltedMarketException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
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
import java.util.List;

public class User {
  private final String email;
  private final String password;
  private final UserRole role;
  private final Cart cart;
  private final Portfolio portfolio;
  private Limit limit;

  public User(String email, String password, UserRole role, Cart cart, Portfolio portfolio, Limit limit) {
    this.email = email;
    this.password = password;
    this.role = role;
    this.cart = cart;
    this.portfolio = portfolio;
    this.limit = limit;
  }

  public String getEmail() {
    return email;
  }

  public boolean isThisYourPassword(String password) {
    return this.password.equals(password);
  }

  public UserRole getRole() {
    return role;
  }

  public Limit getLimit() {
    return limit;
  }

  public void setLimit(Limit limit) {
    this.limit = limit;
  }

  public boolean haveRoleIn(List<UserRole> roles) {
    return roles.contains(role);
  }

  public Cart getCart() {
    return cart;
  }

  public Portfolio getPortfolio() {
    return portfolio;
  }

  public Transaction checkoutCart(TransactionFactory transactionFactory,
                                  MarketRepository marketRepository,
                                  PaymentProcessor paymentProcessor,
                                  StockRepository stockRepository,
                                  NotificationFactory notificationFactory,
                                  NotificationSender notificationSender)
      throws StockNotFoundException, EmptyCartException, TransactionLimitExceededExeption, HaltedMarketException {

    Transaction purchase = cart.checkout(transactionFactory, marketRepository);
    limit.checkIfTransactionExceed(purchase);

    processPurchase(purchase, paymentProcessor, stockRepository);
    sendTransactionNotification(notificationFactory, notificationSender, purchase);

    cart.empty();
    return purchase;
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
