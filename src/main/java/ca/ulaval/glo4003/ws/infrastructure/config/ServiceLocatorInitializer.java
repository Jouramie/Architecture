package ca.ulaval.glo4003.ws.infrastructure.config;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import ca.ulaval.glo4003.ws.domain.user.InMemoryUserRepository;
import ca.ulaval.glo4003.ws.domain.user.UserRepository;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.ws.domain.user.authentication.InMemoryAuthenticationTokenRepository;
import ca.ulaval.glo4003.ws.infrastructure.injection.Component;
import ca.ulaval.glo4003.ws.infrastructure.injection.ErrorMapper;
import ca.ulaval.glo4003.ws.infrastructure.injection.FilterRegistration;
import ca.ulaval.glo4003.ws.infrastructure.injection.ServiceLocator;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import java.util.Arrays;
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

  public void initializeServiceLocator() {
    ServiceLocator.INSTANCE.discoverPackage(packagePrefix, Arrays.asList(Resource.class, ErrorMapper.class, Component.class, FilterRegistration.class));
    ServiceLocator.INSTANCE.registerInstance(UserRepository.class, new InMemoryUserRepository());
    ServiceLocator.INSTANCE.registerInstance(AuthenticationTokenRepository.class, new InMemoryAuthenticationTokenRepository());
    ServiceLocator.INSTANCE.registerInstance(OpenApiResource.class, new OpenApiResource());
  }

  public Set<Object> createInstances() {
    List<Class<?>> registeredClasses = Stream.of(Resource.class, ErrorMapper.class, Component.class)
        .map(annotation -> ServiceLocator.INSTANCE.getClassesForAnnotation(packagePrefix, annotation))
        .flatMap(Collection::stream).collect(toList());
    registeredClasses.add(OpenApiResource.class);

    return registeredClasses.stream()
        .map(ServiceLocator.INSTANCE::get)
        .collect(toSet());
  }
}
