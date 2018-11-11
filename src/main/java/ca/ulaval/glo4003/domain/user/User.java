package ca.ulaval.glo4003.domain.user;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.notification.Notification;
import ca.ulaval.glo4003.domain.notification.NotificationFactory;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.transaction.PaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionFactory;
import ca.ulaval.glo4003.domain.user.exceptions.EmptyCartException;

public class User {
  private final String email;
  private final String password;
  private final UserRole role;
  private final Cart cart;
  private final Portfolio portfolio;

  public User(String email, String password, UserRole role) {
    this.email = email;
    this.password = password;
    this.role = role;
    cart = new Cart();
    portfolio = new Portfolio();
  }

  public String getEmail() {
    return email;
  }

  public UserRole getRole() {
    return role;
  }

  public boolean isThisYourPassword(String password) {
    return this.password.equals(password);
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
                                  NotificationSender notificationSender) throws StockNotFoundException, EmptyCartException {
    checkIfCartIsEmpty(cart);

    Transaction transaction = transactionFactory.createPurchase(cart);
    processTransaction(transaction, paymentProcessor);
    sendTransactionNotification(notificationFactory, notificationSender, transaction);

    cart.empty();

    return transaction;
  }

  private void checkIfCartIsEmpty(Cart cart) throws EmptyCartException {
    if (cart.isEmpty()) {
      throw new EmptyCartException();
    }
  }

  private void processTransaction(Transaction transaction, PaymentProcessor paymentProcessor) {
    paymentProcessor.payment(transaction);
    // TODO: Add transaction to portfolio
  }

  private void sendTransactionNotification(NotificationFactory notificationFactory, NotificationSender notificationSender, Transaction transaction) {
    Notification notification = notificationFactory.create(transaction);
    notificationSender.sendNotification(notification, this);
  }
}
