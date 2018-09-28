package ca.ulaval.glo4003.ws.infrastructure.config;

import ca.ulaval.glo4003.ws.domain.user.InMemoryUserRepository;
import ca.ulaval.glo4003.ws.domain.user.UserRepository;
import ca.ulaval.glo4003.ws.infrastructure.injection.ServiceLocator;

public class ServiceLocatorInitializer {

  public void initializeServiceLocator(ServiceLocator serviceLocator) {
    serviceLocator.discoverPackage("ca.ulaval.glo4003.ws");
    serviceLocator.register(UserRepository.class, InMemoryUserRepository.class);
  }
}
