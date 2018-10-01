package ca.ulaval.glo4003.ws.infrastructure.config;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import ca.ulaval.glo4003.infrastructure.market.MarketRepositoryInMemory;
import ca.ulaval.glo4003.infrastructure.stock.SimulatedStockValueRetriever;
import ca.ulaval.glo4003.infrastructure.stock.StockRepositoryInMemory;
import ca.ulaval.glo4003.ws.domain.user.CurrentUserRepository;
import ca.ulaval.glo4003.ws.domain.user.UserRepository;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.ws.infrastructure.injection.Component;
import ca.ulaval.glo4003.ws.infrastructure.injection.ErrorMapper;
import ca.ulaval.glo4003.ws.infrastructure.injection.FilterRegistration;
import ca.ulaval.glo4003.ws.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.ws.infrastructure.persistence.InMemoryAuthenticationTokenRepository;
import ca.ulaval.glo4003.ws.infrastructure.persistence.InMemoryCurrentUserRepository;
import ca.ulaval.glo4003.ws.infrastructure.persistence.InMemoryUserRepository;
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

  public void initializeServiceLocator(ServiceLocator serviceLocator) {
    serviceLocator.discoverPackage(packagePrefix, Resource.class, ErrorMapper.class, Component.class, FilterRegistration.class);
    serviceLocator.registerInstance(OpenApiResource.class, new OpenApiResource());
    serviceLocator.registerSingleton(UserRepository.class, InMemoryUserRepository.class);
    serviceLocator.registerSingleton(AuthenticationTokenRepository.class, InMemoryAuthenticationTokenRepository.class);
    serviceLocator.registerSingleton(CurrentUserRepository.class, InMemoryCurrentUserRepository.class);
    serviceLocator.registerSingleton(StockRepository.class, StockRepositoryInMemory.class);
    serviceLocator.registerSingleton(MarketRepository.class, MarketRepositoryInMemory.class);
    serviceLocator.registerSingleton(StockValueRetriever.class, SimulatedStockValueRetriever.class);
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
