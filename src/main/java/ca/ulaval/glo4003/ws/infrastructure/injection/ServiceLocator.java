package ca.ulaval.glo4003.ws.infrastructure.injection;

import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.reflections.Reflections;

public class ServiceLocator {

  public static final ServiceLocator INSTANCE = new ServiceLocator();
  private final Map<Class<?>, Class<?>> classes = new ConcurrentHashMap<>();
  private final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();

  public void register(Class<?> registered) {
    register(registered, registered);
  }

  public void register(Class<?> interfaceClass, Class<?> implementedClass) {
    classes.put(interfaceClass, implementedClass);
  }

  public void registerInstance(Class<?> interfaceClass, Object instance) {
    instances.put(interfaceClass, instance);
  }

  public void discoverPackage(String packagePrefix, Class<?>... annotationClasses) {
    Reflections reflections = new Reflections(packagePrefix);
    for (Class annotation : annotationClasses) {
      reflections.getTypesAnnotatedWith(annotation).forEach((foundClass) -> register((Class) foundClass));
    }
  }

  public <T> T get(Class<T> type) {
    if (instances.containsKey(type)) {
      return (T) instances.get(type);
    }
    if (classes.containsKey(type)) {
      return (T) injectConstructor(classes.get(type));
    }
    throw new UnregisteredComponentException(type);
  }

  <T> List<T> getAll(Class<T> type) {
    return (List<T>) Stream.concat(classes.keySet().stream(), instances.keySet().stream())
        .filter(type::isAssignableFrom)
        .map(this::get).collect(toList());
  }

  public Set<Class<?>> getClassesForAnnotation(String packagePrefix, Class<? extends Annotation> annotation) {
    return new HashSet<>(new Reflections(packagePrefix)
        .getTypesAnnotatedWith(annotation));
  }

  private <T> T injectConstructor(Class<T> type) {
    Constructor<?> injectableConstructor = findInjectableConstructorForType(type);
    Object[] parameters = Arrays.stream(injectableConstructor.getParameterTypes()).map(this::get).toArray();
    return instantiateClass(injectableConstructor, parameters);
  }

  private <T> Constructor<?> findInjectableConstructorForType(Class<T> type) {
    return Arrays.stream(type.getDeclaredConstructors())
        .filter(constructor -> constructor.isAnnotationPresent(Inject.class) || constructor.getParameterTypes().length == 0)
        .findFirst()
        .orElseThrow(() -> new NonInjectableConstructorException(type));
  }

  private <T> T instantiateClass(Constructor<?> injectableConstructor, Object[] parameters) {
    try {
      return (T) injectableConstructor.newInstance(parameters);
    } catch (java.lang.InstantiationException | InvocationTargetException | IllegalAccessException e) {
      throw new InstantiationException(injectableConstructor.getDeclaringClass());
    }
  }
}
