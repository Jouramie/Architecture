package ca.ulaval.glo4003.ws.infrastructure.injection;

public class UnregisteredComponentException extends InstantiationException {

    public UnregisteredComponentException(Class<?> type) {
        super(type);
    }
}
