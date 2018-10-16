package ca.ulaval.glo4003.context;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.infrastructure.clock.ClockDriver;
import ca.ulaval.glo4003.infrastructure.config.SimulationSettings;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.infrastructure.market.MarketCsvLoader;
import ca.ulaval.glo4003.infrastructure.market.MarketUpdater;
import ca.ulaval.glo4003.infrastructure.notification.EmailNotificationSender;
import ca.ulaval.glo4003.infrastructure.stock.StockCsvLoader;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import java.io.IOException;

public class ProductionContext extends AbstractContext {
  private ClockDriver clockDriver;

  public ProductionContext(String webServicePackagePrefix, ServiceLocator serviceLocator) {
    super(webServicePackagePrefix, serviceLocator);
  }

  @Override
  public void configureApplication() {
    super.configureApplication();
    startClockAndMarketsUpdater();
    serviceLocator.registerInstance(NotificationSender.class, createAwsSesSender());
  }

  @Override
  protected void loadData() {
    try {
      loadCsvData();
      createAdministrator();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void createAdministrator() {
    String testEmail = "Archi.test.42@gmail.com";
    try {
      serviceLocator.get(UserRepository.class)
          .add(new User(testEmail, "asdf", UserRole.ADMINISTRATOR));
    } catch (UserAlreadyExistsException exception) {
      System.out.println("Test user couldn't be added");
      exception.printStackTrace();
    }
    serviceLocator.get(AuthenticationTokenRepository.class)
        .add(new AuthenticationToken("00000000-0000-0000-0000-000000000000", testEmail));
  }

  private void loadCsvData() throws IOException, MarketNotFoundException {
    MarketCsvLoader marketLoader = new MarketCsvLoader(
        serviceLocator.get(MarketRepository.class),
        serviceLocator.get(StockRepository.class),
        serviceLocator.get(StockValueRetriever.class));
    marketLoader.load();

    StockCsvLoader stockLoader = new StockCsvLoader(
        serviceLocator.get(StockRepository.class),
        serviceLocator.get(MarketRepository.class));
    stockLoader.load();
  }

  private void startClockAndMarketsUpdater() {
    clockDriver = new ClockDriver(
        serviceLocator.get(Clock.class),
        SimulationSettings.SIMULATION_UPDATE_FREQUENCY);
    new MarketUpdater(
        serviceLocator.get(Clock.class),
        serviceLocator.get(MarketRepository.class));
    clockDriver.start();
  }

  private EmailNotificationSender createAwsSesSender() {
    AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
        .withRegion(Regions.US_EAST_1).build();
    return new EmailNotificationSender(client);
  }

  @Override
  public void configureDestruction() {
    super.configureDestruction();
    clockDriver.stop();
  }
}
