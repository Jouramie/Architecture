package ca.ulaval.glo4003.context;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.clock.ReadableClock;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.MarketUpdater;
import ca.ulaval.glo4003.domain.notification.NotificationSender;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import ca.ulaval.glo4003.domain.transaction.NullPaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.PaymentProcessor;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.UserFactory;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.domain.user.exceptions.UserAlreadyExistsException;
import ca.ulaval.glo4003.infrastructure.clock.ClockDriver;
import ca.ulaval.glo4003.infrastructure.config.SimulationSettings;
import ca.ulaval.glo4003.infrastructure.config.StocksDataSettings;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocatorInitializer;
import ca.ulaval.glo4003.infrastructure.market.MarketCsvLoader;
import ca.ulaval.glo4003.infrastructure.notification.AwsEmailNotificationSender;
import ca.ulaval.glo4003.infrastructure.persistence.InMemoryAuthenticationTokenRepository;
import ca.ulaval.glo4003.infrastructure.persistence.InMemoryMarketRepository;
import ca.ulaval.glo4003.infrastructure.persistence.InMemoryStockRepository;
import ca.ulaval.glo4003.infrastructure.persistence.InMemoryUserRepository;
import ca.ulaval.glo4003.infrastructure.stock.SimulatedStockValueRetriever;
import ca.ulaval.glo4003.infrastructure.stock.StockCsvLoader;
import ca.ulaval.glo4003.investul.live_stock_emulator.StockSimulator;
import ca.ulaval.glo4003.ws.api.ErrorMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Stream;
import javax.annotation.Resource;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

public class ProductionContext extends AbstractContext {
  public static final String DEFAULT_ADMIN_EMAIL = "Archi.test.43@gmail.com";
  public static final String DEFAULT_ADMIN_PASSWORD = "asdfasdf";
  private static final String WEB_SERVICE_PACKAGE_PREFIX = "ca.ulaval.glo4003";
  private ClockDriver clockDriver;

  public ProductionContext(JerseyApiHandlersCreator jerseyApiHandlersCreator) {
    super(jerseyApiHandlersCreator);
  }

  @Override
  public void configureDestruction() {
    clockDriver.stop();
  }

  @Override
  public void configureApplication(String apiUrl) {
    super.configureApplication(apiUrl);
    createSwaggerApi(apiUrl);
    startClockAndMarketsUpdater();
  }

  private void createSwaggerApi(String apiUrl) {
    OpenAPI swaggerOpenApi = new OpenAPI();
    Info info = new Info()
        .title("Invest-UL")
        .description("Logiciel transactionnel pour titres boursiers");

    Server server = new Server();
    server.setUrl(apiUrl);

    swaggerOpenApi
        .info(info)
        .servers(Stream.of(server).collect(toList()));

    SwaggerConfiguration oasConfig = new SwaggerConfiguration()
        .openAPI(swaggerOpenApi)
        .prettyPrint(true);

    try {
      new JaxrsOpenApiContextBuilder()
          .openApiConfiguration(oasConfig)
          .buildContext(true);
    } catch (OpenApiConfigurationException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
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

  @Override
  public ContextHandlerCollection createJettyContextHandlers() {
    ContextHandlerCollection contexts = new ContextHandlerCollection();
    jerseyApiHandlersCreator.createHandlers(serviceLocator, WEB_SERVICE_PACKAGE_PREFIX).forEach(contexts::addHandler);
    return contexts;
  }

  @Override
  protected ServiceLocatorInitializer createServiceLocatorInitializer() {
    Clock clock = createClock();

    return new ServiceLocatorInitializer(serviceLocator).discoverPackage(WEB_SERVICE_PACKAGE_PREFIX,
        Resource.class, ErrorMapper.class, Component.class)

        .registerInstance(OpenApiResource.class, new OpenApiResource())
        .registerInstance(StockSimulator.class, new StockSimulator())
        .register(UserRepository.class, InMemoryUserRepository.class)
        .register(AuthenticationTokenRepository.class, InMemoryAuthenticationTokenRepository.class)
        .register(StockRepository.class, InMemoryStockRepository.class)
        .register(MarketRepository.class, InMemoryMarketRepository.class)
        .register(StockValueRetriever.class, SimulatedStockValueRetriever.class)
        .register(PaymentProcessor.class, NullPaymentProcessor.class)
        .register(CurrentUserSession.class, CurrentUserSession.class)
        .registerInstance(ReadableClock.class, clock)
        .registerInstance(Clock.class, clock)
        .registerInstance(NotificationSender.class, createAwsSesSender());
  }

  private Clock createClock() {
    LocalDate startDate = StocksDataSettings.LAST_STOCK_DATA_DATE;
    return new Clock(startDate.atTime(0, 0, 0), SimulationSettings.CLOCK_TICK_DURATION);
  }

  private AwsEmailNotificationSender createAwsSesSender() {
    AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
        .withRegion(Regions.US_EAST_1).build();
    return new AwsEmailNotificationSender(client);
  }

  @Override
  protected void loadData() {
    try {
      loadCsvData();
      createUsers();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void createUsers() {
    try {
      UserRepository userRepository = serviceLocator.get(UserRepository.class);
      UserFactory userFactory = serviceLocator.get(UserFactory.class);
      userRepository.add(userFactory.createAdministrator(DEFAULT_ADMIN_EMAIL, DEFAULT_ADMIN_PASSWORD));
    } catch (UserAlreadyExistsException exception) {
      System.out.println("Admin user couldn't be added");
      exception.printStackTrace();
    }
  }

  private void loadCsvData() throws IOException, MarketNotFoundException {
    MarketCsvLoader marketLoader = new MarketCsvLoader(
        serviceLocator.get(MarketRepository.class),
        serviceLocator.get(StockRepository.class));
    marketLoader.load(StocksDataSettings.STOCKS_DATA_PATH);

    StockCsvLoader stockLoader = new StockCsvLoader(
        serviceLocator.get(StockRepository.class),
        serviceLocator.get(MarketRepository.class));
    stockLoader.load(StocksDataSettings.STOCKS_DATA_PATH, StocksDataSettings.LAST_STOCK_DATA_DATE);
  }
}
