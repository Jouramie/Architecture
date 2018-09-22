package ca.ulaval.glo4003.ws.infrastructure.config;

import ca.ulaval.glo4003.ws.api.PingResource;
import ca.ulaval.glo4003.ws.api.PingResourceImpl;
import ca.ulaval.glo4003.ws.api.authentication.UserResource;
import ca.ulaval.glo4003.ws.api.authentication.UserResourceImpl;
import ca.ulaval.glo4003.ws.domain.user.InMemoryUserRepository;
import ca.ulaval.glo4003.ws.domain.user.UserRepository;
import ca.ulaval.glo4003.ws.infrastructure.injection.ServiceLocator;

public class ServiceLocatorInitializer {

    public void initializeServiceLocator(ServiceLocator serviceLocator) {
        serviceLocator.discoverPackage("ca.ulaval.glo4003.ws");

        serviceLocator.register(UserResource.class, UserResourceImpl.class);
        serviceLocator.register(PingResource.class, PingResourceImpl.class);
        serviceLocator.register(UserRepository.class, InMemoryUserRepository.class);
    }
}
