package ca.ulaval.glo4003.portfolio;

import ca.ulaval.glo4003.context.AbstractContext;
import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.notification.NullNotificationSender;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.service.cart.CartService;
import ca.ulaval.glo4003.service.cart.CheckoutService;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PortfolioReportITContext extends AbstractContext {
  @Override
  public void configureApplication(String apiUrl) {
    super.configureApplication(apiUrl);
    serviceLocator.register(NotificationSender.class, NullNotificationSender.class);

    setCurrentUserToDefaultAdmin();
    addStocksToCurrentUserPortfolio();
  }

  private void setCurrentUserToDefaultAdmin() {
    User user = null;
    try {
      user = serviceLocator.get(UserRepository.class).find(DEFAULT_ADMIN_EMAIL);
    } catch (UserNotFoundException e) {
      e.printStackTrace();
    }
    serviceLocator.get(CurrentUserSession.class).setCurrentUser(user);
  }

  private void addStocksToCurrentUserPortfolio() {
    LocalDate currentDate = serviceLocator.get(Clock.class).getCurrentTime().toLocalDate();
    addStock("RBS.l", 6, currentDate.minusDays(45));
    addStock("AAPL", 12, currentDate.minusDays(15));
    addStock("MSFT", 24, currentDate.minusDays(3));
  }

  private void addStock(String title, int quantity, LocalDate transactionDate) {
    Clock clock = serviceLocator.get(Clock.class);
    LocalDateTime oldDateTime = clock.getCurrentTime();

    clock.setCurrentTime(transactionDate.atTime(0, 0, 0));
    performTransaction(title, quantity);
    clock.setCurrentTime(oldDateTime);
  }

  private void performTransaction(String title, int quantity) {
    serviceLocator.get(CartService.class).addStockToCart(title, quantity);
    serviceLocator.get(CheckoutService.class).checkoutCart();
  }
}
