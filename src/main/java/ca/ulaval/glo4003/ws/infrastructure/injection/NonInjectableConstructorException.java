package ca.ulaval.glo4003.ws.infrastructure.injection;

public class NonInjectableConstructorException extends InstantiationException {
    public NonInjectableConstructorException(Class<?> type) {
        super(type);
    }
}
