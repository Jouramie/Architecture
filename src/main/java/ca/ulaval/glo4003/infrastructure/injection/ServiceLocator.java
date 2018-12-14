package ca.ulaval.glo4003.infrastructure.injection;

import ca.ulaval.glo4003.infrastructure.injection.exception.UnregisteredComponentException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceLocator {

  public static final ServiceLocator INSTANCE = new ServiceLocator();
  private Map<Class<?>, Object> instances = new ConcurrentHashMap<>();

  public void registerInstance(Class<?> interfaceClass, Object instance) {
    instances.put(interfaceClass, instance);
  }

  public <T> T get(Class<T> type) {
    return (T) Optional.ofNullable(instances.get(type)).orElseThrow(() -> new UnregisteredComponentException(type));
  }

  public void reset() {
    instances = new ConcurrentHashMap<>();
  }
}
