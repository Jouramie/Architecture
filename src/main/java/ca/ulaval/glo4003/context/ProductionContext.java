package ca.ulaval.glo4003.context;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.MarketUpdater;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import ca.ulaval.glo4003.infrastructure.clock.ClockDriver;
import ca.ulaval.glo4003.infrastructure.config.SimulationSettings;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocatorInitializer;
import ca.ulaval.glo4003.infrastructure.notification.AwsEmailNotificationSender;
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
    Clock clock = serviceLocator.get(Clock.class);
    clockDriver = new ClockDriver(clock, SimulationSettings.SIMULATION_UPDATE_FREQUENCY);
    clock.register(new MarketUpdater(
        serviceLocator.get(MarketRepository.class),
        serviceLocator.get(StockValueRetriever.class)
    ));
    clockDriver.start();
  }

  private AwsEmailNotificationSender createAwsSesSender() {
    AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
        .withRegion(Regions.US_EAST_1).build();
    return new AwsEmailNotificationSender(client);
  }

  @Override
  public void configureDestruction() {
    super.configureDestruction();
    clockDriver.stop();
  }
}
