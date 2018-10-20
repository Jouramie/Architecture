package ca.ulaval.glo4003.infrasctructure.injection;

public class UnregisteredComponentException extends InstantiationException {

  UnregisteredComponentException(Class<?> type) {
    super(type);
  }
}
