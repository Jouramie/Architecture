package ca.ulaval.glo4003.service.cart;

import ca.ulaval.glo4003.domain.Component;
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
import ca.ulaval.glo4003.service.cart.assemblers.TransactionAssembler;
import ca.ulaval.glo4003.service.cart.dto.TransactionDto;
import ca.ulaval.glo4003.service.cart.exceptions.EmptyCartOnCheckoutException;
import ca.ulaval.glo4003.service.cart.exceptions.InvalidStockTitleException;
import javax.inject.Inject;

@Component
public class CheckoutService {
  private final CurrentUserSession currentUserSession;
  private final TransactionFactory transactionFactory;
  private final PaymentProcessor paymentProcessor;
  private final NotificationFactory notificationFactory;
  private final NotificationSender notificationSender;
  private final TransactionAssembler transactionAssembler;
  private final StockRepository stockRepository;

  @Inject
  public CheckoutService(CurrentUserSession currentUserSession,
                         TransactionFactory transactionFactory,
                         PaymentProcessor paymentProcessor,
                         NotificationFactory notificationFactory,
                         NotificationSender notificationSender,
                         TransactionAssembler transactionAssembler,
                         StockRepository stockRepository) {
    this.currentUserSession = currentUserSession;
    this.transactionFactory = transactionFactory;
    this.paymentProcessor = paymentProcessor;
    this.notificationFactory = notificationFactory;
    this.notificationSender = notificationSender;
    this.transactionAssembler = transactionAssembler;
    this.stockRepository = stockRepository;
  }

  public TransactionDto checkoutCart() throws InvalidStockTitleException {
    User currentUser = currentUserSession.getCurrentUser();
    try {
      Transaction transaction = currentUser.checkoutCart(
          transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);
      return transactionAssembler.toDto(transaction);
    } catch (StockNotFoundException e) {
      throw new InvalidStockTitleException(e.title);
    } catch (EmptyCartException e) {
      throw new EmptyCartOnCheckoutException();
    }
  }
}
