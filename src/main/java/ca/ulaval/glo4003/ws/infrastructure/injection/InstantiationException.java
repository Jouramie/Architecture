package ca.ulaval.glo4003.ws.infrastructure.injection;

public class InstantiationException extends RuntimeException {

    public InstantiationException(Class<?> type) {
        super(String.format("Cannot instantiate object of class %s.", type.toString()));
    }
}
