package ca.ulaval.glo4003.ws.infrastructure.injection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.reflections.Reflections;

public class ServiceLocator {

    private Map<Class<?>, Class<?>> classes = new ConcurrentHashMap<>();
    private Map<Class<?>, Object> instances = new ConcurrentHashMap<>();

    public void register(Class<?> registered) {
        register(registered, registered);
    }

    public void register(Class<?> interfaceClass, Class<?> implementedClass) {
        classes.put(interfaceClass, implementedClass);
    }

    public void registerInstance(Class<?> interfaceClass, Object instance) {
        instances.put(interfaceClass, instance);
    }

    public void discoverPackage(String packagePrefix) {
        new Reflections(packagePrefix).getTypesAnnotatedWith(Component.class).forEach(this::register);
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

    public <T> List<T> getAll(Class<T> type) {
        return (List<T>) Stream.concat(classes.keySet().stream(), instances.keySet().stream())
            .filter((registeredClass) -> type.isAssignableFrom(registeredClass))
            .map(this::get).collect(Collectors.toList());
    }

    private <T> T injectConstructor(Class<T> type) {
        Optional<Constructor<?>> injectableConstructor =
            Arrays.stream(type.getDeclaredConstructors())
                .filter((constructor) -> constructor.isAnnotationPresent(Inject.class))
                .findFirst();
        if (!injectableConstructor.isPresent()) {
            throw new NonInjectableConstructorException(type);
        }
        Constructor<?> constructor = injectableConstructor.get();
        Object[] parameters = Arrays.stream(constructor.getParameterTypes()).map(this::get).toArray();
        try {
            return (T) constructor.newInstance(parameters);
        } catch (java.lang.InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new InstantiationException(type);
        }
    }
}
