package ca.ulaval.glo4003.service.cart;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.cart.EmptyCartException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.exception.HaltedMarketException;
import ca.ulaval.glo4003.domain.notification.NotificationFactory;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.exception.StockNotFoundException;
import ca.ulaval.glo4003.domain.transaction.PaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionFactory;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.domain.user.limit.TransactionLimitExceededExeption;
import ca.ulaval.glo4003.service.cart.exception.EmptyCartOnCheckoutException;
import ca.ulaval.glo4003.service.cart.exception.HaltedMarketOnCheckoutException;
import ca.ulaval.glo4003.service.cart.exception.InvalidStockTitleException;
import ca.ulaval.glo4003.service.cart.exception.PurchaseLimitExceededOnCheckoutException;
import ca.ulaval.glo4003.service.transaction.TransactionAssembler;
import ca.ulaval.glo4003.service.transaction.TransactionDto;
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
  private final MarketRepository marketRepository;

  @Inject
  public CheckoutService(CurrentUserSession currentUserSession,
                         TransactionFactory transactionFactory,
                         PaymentProcessor paymentProcessor,
                         NotificationFactory notificationFactory,
                         NotificationSender notificationSender,
                         TransactionAssembler transactionAssembler,
                         StockRepository stockRepository,
                         MarketRepository marketRepository) {
    this.currentUserSession = currentUserSession;
    this.transactionFactory = transactionFactory;
    this.paymentProcessor = paymentProcessor;
    this.notificationFactory = notificationFactory;
    this.notificationSender = notificationSender;
    this.transactionAssembler = transactionAssembler;
    this.stockRepository = stockRepository;
    this.marketRepository = marketRepository;
  }

  public TransactionDto checkoutCart() throws InvalidStockTitleException {
    Investor currentInvestor = currentUserSession.getCurrentUser(Investor.class);
    try {
      Transaction transaction = currentInvestor.checkoutCart(transactionFactory, marketRepository,
          paymentProcessor, stockRepository, notificationFactory, notificationSender);
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
