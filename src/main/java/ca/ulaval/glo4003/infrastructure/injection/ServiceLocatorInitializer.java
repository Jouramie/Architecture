package ca.ulaval.glo4003.infrastructure.injection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.reflections.Reflections;

public class ServiceLocatorInitializer {

  private final ServiceLocator serviceLocator;
  private final Map<Class<?>, Class<?>> registeredClasses;
  private final Map<Class<?>, Object> objects;

  public ServiceLocatorInitializer(ServiceLocator serviceLocator) {
    this.serviceLocator = serviceLocator;
    registeredClasses = new ConcurrentHashMap<>();
    objects = new ConcurrentHashMap<>();
  }

  public <T> ServiceLocatorInitializer register(Class<T> clazz) {
    return register(clazz, clazz);
  }

  public <T> ServiceLocatorInitializer register(Class<T> abstraction, Class<? extends T> implementation) {
    registeredClasses.put(abstraction, implementation);
    return this;
  }

  public <T> ServiceLocatorInitializer registerInstance(Class<T> abstraction, Object instance) {
    objects.put(abstraction, instance);
    return this;
  }

  public ServiceLocatorInitializer discoverPackage(String packagePrefix, Class<?>... annotationClasses) {
    Reflections reflections = new Reflections(packagePrefix);

    for (Class annotation : annotationClasses) {
      reflections.getTypesAnnotatedWith(annotation).forEach((foundClass) -> register((Class) foundClass));
    }
    return this;
  }

  public void configure() {
    Stream.concat(registeredClasses.keySet().stream(), objects.keySet().stream()).forEach((clazz) -> serviceLocator.registerInstance(clazz, resolve(clazz)));
  }

  private <T> T resolve(Class<? extends T> type) {
    if (objects.containsKey(type)) {
      return (T) objects.get(type);
    }
    T instance = instantiate((Class<T>) registeredClasses.get(type));
    objects.put(type, instance);
    return instance;
  }

  private <T> T instantiate(Class<T> type) {
    Constructor<?> injectableConstructor = findInjectableConstructorForType(type);
    Object[] parameters = Arrays.stream(injectableConstructor.getParameterTypes()).map(this::resolve)
        .toArray();
    return injectConstructor(injectableConstructor, parameters);
  }

  private <T> Constructor<?> findInjectableConstructorForType(Class<T> type) {
    return Arrays.stream(type.getDeclaredConstructors())
        .filter(constructor -> constructor.isAnnotationPresent(Inject.class)
            || constructor.getParameterTypes().length == 0)
        .findFirst()
        .orElseThrow(() -> new NonInjectableConstructorException(type));
  }

  private <T> T injectConstructor(Constructor<?> injectableConstructor, Object[] parameters) {
    try {
      return (T) injectableConstructor.newInstance(parameters);
    } catch (java.lang.InstantiationException | InvocationTargetException | IllegalAccessException e) {
      throw new InstantiationException(injectableConstructor.getDeclaringClass());
    }
  }
}
