package ca.ulaval.glo4003.ws.infrastructure.injection;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import javax.annotation.Resource;
import javax.inject.Inject;
import org.reflections.Reflections;

public class ServiceLocator {

  private final List<String> packagesDiscovered;

  private final Map<Class<?>, Class<?>> classes = new ConcurrentHashMap<>();
  private final Map<Class<?>, Class<?>> singletons = new ConcurrentHashMap<>();
  private final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();

  public ServiceLocator() {
    packagesDiscovered = new ArrayList<>();
  }

  public void register(Class<?> registered) {
    register(registered, registered);
  }

  public void register(Class<?> interfaceClass, Class<?> implementedClass) {
    classes.put(interfaceClass, implementedClass);
  }

  public void registerSingleton(Class<?> interfaceClass, Class<?> implementedClass) {
    singletons.put(interfaceClass, implementedClass);
  }

  public void registerInstance(Class<?> interfaceClass, Object instance) {
    instances.put(interfaceClass, instance);
  }

  public void discoverPackage(String packagePrefix) {
    Reflections reflections = new Reflections(packagePrefix);
    reflections.getTypesAnnotatedWith(Component.class).forEach(this::register);
    reflections.getTypesAnnotatedWith(ErrorMapper.class).forEach(this::register);
    reflections.getTypesAnnotatedWith(Resource.class).forEach(this::register);
    packagesDiscovered.add(packagePrefix);
  }

  public <T> T get(Class<T> type) {
    if (instances.containsKey(type)) {
      return (T) instances.get(type);
    }
    if (singletons.containsKey(type)) {
      T instance = (T) injectConstructor(singletons.get(type));
      instances.put(type, instance);
      singletons.remove(type);
      return instance;
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

  public Set<?> getAllClassesForAnnotation(Class<? extends Annotation> annotation) {
    return packagesDiscovered.stream().map(Reflections::new)
        .map(reflection -> reflection.getTypesAnnotatedWith(annotation))
        .flatMap(Collection::stream)
        .map(this::get)
        .collect(toSet());
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
