package ca.ulaval.glo4003.infrastructure.config;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import ca.ulaval.glo4003.domain.transaction.NullPaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.PaymentProcessor;
import ca.ulaval.glo4003.domain.transaction.TransactionLedger;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.infrastructure.injection.Component;
import ca.ulaval.glo4003.infrastructure.injection.ErrorMapper;
import ca.ulaval.glo4003.infrastructure.injection.FilterRegistration;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.infrastructure.persistence.InMemoryAuthenticationTokenRepository;
import ca.ulaval.glo4003.infrastructure.persistence.InMemoryMarketRepository;
import ca.ulaval.glo4003.infrastructure.persistence.InMemoryStockRepository;
import ca.ulaval.glo4003.infrastructure.persistence.InMemoryTransactionLedger;
import ca.ulaval.glo4003.infrastructure.persistence.InMemoryUserRepository;
import ca.ulaval.glo4003.infrastructure.stock.SimulatedStockValueRetriever;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Resource;

public class ServiceLocatorInitializer {

  private final String packagePrefix;

  public ServiceLocatorInitializer(String packagePrefix) {
    this.packagePrefix = packagePrefix;
  }

  public void initialize(ServiceLocator serviceLocator) {
    serviceLocator.discoverPackage(packagePrefix, Resource.class, ErrorMapper.class,
        Component.class, FilterRegistration.class);
    serviceLocator.registerInstance(OpenApiResource.class, new OpenApiResource());
    serviceLocator.registerSingleton(UserRepository.class, InMemoryUserRepository.class);
    serviceLocator.registerSingleton(AuthenticationTokenRepository.class,
        InMemoryAuthenticationTokenRepository.class);
    serviceLocator.registerSingleton(CurrentUserSession.class, CurrentUserSession.class);
    serviceLocator.registerSingleton(StockRepository.class, InMemoryStockRepository.class);
    serviceLocator.registerSingleton(MarketRepository.class, InMemoryMarketRepository.class);
    serviceLocator.registerSingleton(StockValueRetriever.class, SimulatedStockValueRetriever.class);
    serviceLocator.registerSingleton(TransactionLedger.class, InMemoryTransactionLedger.class);
    serviceLocator.registerSingleton(PaymentProcessor.class, NullPaymentProcessor.class);
  }

  public Set<Object> createInstances(ServiceLocator serviceLocator) {
    List<Class<?>> registeredClasses = Stream.of(Resource.class, ErrorMapper.class, Component.class)
        .map(annotation -> serviceLocator.getClassesForAnnotation(packagePrefix, annotation))
        .flatMap(Collection::stream).collect(toList());
    registeredClasses.add(OpenApiResource.class);

    return registeredClasses.stream()
        .map(serviceLocator::get)
        .collect(toSet());
  }
}
