package ca.ulaval.glo4003.context;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.MarketUpdater;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import ca.ulaval.glo4003.infrastructure.clock.ClockDriver;
import ca.ulaval.glo4003.infrastructure.config.SimulationSettings;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocatorInitializer;
import ca.ulaval.glo4003.infrastructure.notification.EmailNotificationSender;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;

public class ProductionContext extends AbstractContext {
  private ClockDriver clockDriver;

  @Override
  public void configureApplication(String apiUrl) {
    super.configureApplication(apiUrl);
    startClockAndMarketsUpdater();
  }

  @Override
  protected ServiceLocatorInitializer createServiceLocatorInitializer() {
    return super.createServiceLocatorInitializer().registerInstance(NotificationSender.class, createAwsSesSender());
  }

  private void startClockAndMarketsUpdater() {
    clockDriver = new ClockDriver(
        serviceLocator.get(Clock.class),
        SimulationSettings.SIMULATION_UPDATE_FREQUENCY);
    MarketUpdater marketUpdater = new MarketUpdater(
        serviceLocator.get(MarketRepository.class),
        serviceLocator.get(StockValueRetriever.class)
    );
    serviceLocator.get(Clock.class).register(marketUpdater);
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
