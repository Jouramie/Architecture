package ca.ulaval.glo4003.ws.infrastructure.config;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import ca.ulaval.glo4003.ws.domain.user.InMemoryUserRepository;
import ca.ulaval.glo4003.ws.domain.user.UserRepository;
import ca.ulaval.glo4003.ws.infrastructure.injection.Component;
import ca.ulaval.glo4003.ws.infrastructure.injection.ErrorMapper;
import ca.ulaval.glo4003.ws.infrastructure.injection.ServiceLocator;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Resource;

public class ServiceLocatorInitializer {

  private final String packagePrefix;

  public ServiceLocatorInitializer(String packagePrefix) {
    this.packagePrefix = packagePrefix;
  }

  public void initializeServiceLocator(ServiceLocator serviceLocator) {
    serviceLocator.discoverPackage(packagePrefix, Stream.of(Resource.class, ErrorMapper.class, Component.class).collect(toList()));
    serviceLocator.register(UserRepository.class, InMemoryUserRepository.class);
  }

  public Set<Object> createInstances(ServiceLocator serviceLocator) {
    Set<?> resourceInstances = serviceLocator.getInstancesForAnnotation(packagePrefix, Resource.class);
    Set<?> errorMapperClasses = serviceLocator.getInstancesForAnnotation(packagePrefix, ErrorMapper.class);
    Set<?> componentClasses = serviceLocator.getInstancesForAnnotation(packagePrefix, Component.class);
    return Stream.of(resourceInstances, errorMapperClasses, componentClasses, Collections.singletonList(new OpenApiResource()))
        .flatMap(Collection::stream)
        .collect(toSet());
  }
}
