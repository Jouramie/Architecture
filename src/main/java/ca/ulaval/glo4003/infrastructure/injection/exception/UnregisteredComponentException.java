package ca.ulaval.glo4003.infrastructure.injection.exception;

public class UnregisteredComponentException extends InjectionInstantiationException {

  public UnregisteredComponentException(Class<?> type) {
    super(type);
  }
}
