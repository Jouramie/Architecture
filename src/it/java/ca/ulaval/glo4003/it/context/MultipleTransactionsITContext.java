package ca.ulaval.glo4003.it.context;

import static ca.ulaval.glo4003.infrastructure.config.StocksDataSettings.LAST_STOCK_DATA_DATE;

import ca.ulaval.glo4003.context.AbstractContext;
import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.clock.ReadableClock;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.notification.NullNotificationSender;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocatorInitializer;
import ca.ulaval.glo4003.it.util.TestClock;
import ca.ulaval.glo4003.service.cart.CartService;
import ca.ulaval.glo4003.service.cart.CheckoutService;
import java.time.LocalDate;

public class MultipleTransactionsITContext extends AbstractContext {

  private TestClock testClock;

  @Override
  public void configureApplication(String apiUrl) {
    super.configureApplication(apiUrl);

    setCurrentUserToDefaultInvestor();
    addStocksToCurrentUserPortfolio();
  }

  @Override
  protected Clock createClock() {
    testClock = new TestClock(LAST_STOCK_DATA_DATE.atTime(0, 0, 0));
    return testClock;
  }

  @Override
  protected ServiceLocatorInitializer createServiceLocatorInitializer() {
    return super.createServiceLocatorInitializer().register(NotificationSender.class, NullNotificationSender.class);
  }

  private void setCurrentUserToDefaultInvestor() {
    User user = null;
    try {
      user = serviceLocator.get(UserRepository.class).findByEmail(DEFAULT_INVESTOR_EMAIL);
    } catch (UserNotFoundException e) {
      e.printStackTrace();
    }
    serviceLocator.get(CurrentUserSession.class).setCurrentUser(user);
  }

  private void addStocksToCurrentUserPortfolio() {
    LocalDate currentDate = serviceLocator.get(ReadableClock.class).getCurrentTime().toLocalDate();
    addStock("RBS.l", 6, currentDate.minusDays(45));
    addStock("AAPL", 12, currentDate.minusDays(15));
    addStock("MSFT", 24, currentDate.minusDays(3));
  }

  private void addStock(String title, int quantity, LocalDate transactionDate) {
    testClock.setCurrentTime(transactionDate.atTime(0, 0, 0));
    performTransaction(title, quantity);
  }

  private void performTransaction(String title, int quantity) {
    serviceLocator.get(CartService.class).addStockToCart(title, quantity);
    serviceLocator.get(CheckoutService.class).checkoutCart();
  }
}
