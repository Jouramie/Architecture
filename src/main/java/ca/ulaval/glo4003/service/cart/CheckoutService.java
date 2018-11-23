package ca.ulaval.glo4003.service.cart;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.market.HaltedMarketException;
import ca.ulaval.glo4003.domain.notification.NotificationFactory;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.transaction.PaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionFactory;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.domain.user.exceptions.EmptyCartException;
import ca.ulaval.glo4003.domain.user.limit.TransactionLimitExceededExeption;
import ca.ulaval.glo4003.service.cart.assemblers.TransactionAssembler;
import ca.ulaval.glo4003.service.cart.dto.TransactionDto;
import ca.ulaval.glo4003.service.cart.exceptions.EmptyCartOnCheckoutException;
import ca.ulaval.glo4003.service.cart.exceptions.InvalidStockTitleException;
import ca.ulaval.glo4003.service.cart.exceptions.PurchaseLimitExceededOnCheckoutException;
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
    Investor currentInvestor = currentUserSession.getCurrentUser(Investor.class);
    try {
      Transaction transaction = currentInvestor.checkoutCart(
          transactionFactory, paymentProcessor, notificationFactory, notificationSender, stockRepository);
      return transactionAssembler.toDto(transaction);
    } catch (StockNotFoundException e) {
      throw new InvalidStockTitleException(e.title);
    } catch (EmptyCartException e) {
      throw new EmptyCartOnCheckoutException();
    } catch (HaltedMarketException e) {
      throw new HaltedMarketOnCheckoutException(e.message);
    } catch (TransactionLimitExceededExeption e) {
      throw new PurchaseLimitExceededOnCheckoutException();
    }
  }
}
