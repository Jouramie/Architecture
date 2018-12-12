package ca.ulaval.glo4003.it.portfolio;

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
import ca.ulaval.glo4003.service.cart.CartService;
import ca.ulaval.glo4003.service.cart.CheckoutService;
import java.time.Duration;
import java.time.LocalDate;

public class MultipleTransactionsITContext extends AbstractContext {
  @Override
  public void configureApplication(String apiUrl) {
    super.configureApplication(apiUrl);

    setCurrentUserToDefaultInvestor();
    addStocksToCurrentUserPortfolio();
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
    ReadableClock currentClock = serviceLocator.get(ReadableClock.class);
    Clock testClock = new Clock(transactionDate.atTime(0, 0, 0), Duration.ZERO);

    serviceLocator.registerInstance(ReadableClock.class, testClock);
    performTransaction(title, quantity);
    serviceLocator.registerInstance(ReadableClock.class, currentClock);
  }

  private void performTransaction(String title, int quantity) {
    serviceLocator.get(CartService.class).addStockToCart(title, quantity);
    serviceLocator.get(CheckoutService.class).checkoutCart();
  }
}
