package ca.ulaval.glo4003.infrastructure.injection;

public class UnregisteredComponentException extends InstantiationException {

  UnregisteredComponentException(Class<?> type) {
    super(type);
  }
}
