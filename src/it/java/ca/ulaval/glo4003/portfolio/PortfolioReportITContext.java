package ca.ulaval.glo4003.portfolio;

import ca.ulaval.glo4003.context.AbstractContext;
import ca.ulaval.glo4003.domain.clock.Clock;
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

    LocalDate currentDate = serviceLocator.get(Clock.class).getCurrentTime().toLocalDate();
    addStock("MSFT", 24, currentDate);
    addStock("APPL", 12, currentDate.minusDays(1));
    addStock("RBS.1", 6, currentDate.minusDays(45));
  }

  private void addStock(String title, int quantity, LocalDate transactionDate) {
    // Get current user
    User user = null;
    try {
      user = serviceLocator.get(UserRepository.class).find(DEFAULT_ADMIN_EMAIL);
    } catch (UserNotFoundException e) {
      e.printStackTrace();
    }
    serviceLocator.get(CurrentUserSession.class).setCurrentUser(user);

    // Set clock to transaction date
    Clock clock = serviceLocator.get(Clock.class);
    LocalDateTime oldDateTime = clock.getCurrentTime();
    clock.setCurrentTime(transactionDate.atTime(0, 0, 0));

    // Perform transaction
    serviceLocator.get(CartService.class).addStockToCart(title, quantity);
    serviceLocator.get(CheckoutService.class).checkoutCart();

    // Revert clock to old time
    clock.setCurrentTime(oldDateTime);
  }
}
