package ca.ulaval.glo4003.ws.infrastructure.config;

import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import ca.ulaval.glo4003.infrastructure.market.MarketRepositoryInMemory;
import ca.ulaval.glo4003.infrastructure.stock.SimulatedStockValueRetriever;
import ca.ulaval.glo4003.infrastructure.stock.StockRepositoryInMemory;
import ca.ulaval.glo4003.ws.domain.user.UserRepository;
import ca.ulaval.glo4003.ws.domain.user.UserRepositoryInMemory;
import ca.ulaval.glo4003.ws.infrastructure.injection.ServiceLocator;

public class ServiceLocatorInitializer {

  public void initializeServiceLocator(ServiceLocator serviceLocator) {
    serviceLocator.discoverPackage("ca.ulaval.glo4003");
    serviceLocator.registerSingleton(UserRepository.class, UserRepositoryInMemory.class);
    serviceLocator.registerSingleton(StockRepository.class, StockRepositoryInMemory.class);
    serviceLocator.registerSingleton(MarketRepository.class, MarketRepositoryInMemory.class);
    serviceLocator.registerSingleton(StockValueRetriever.class, SimulatedStockValueRetriever.class);
  }
}
