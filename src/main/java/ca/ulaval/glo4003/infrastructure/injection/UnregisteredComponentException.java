package ca.ulaval.glo4003.infrastructure.injection;

public class UnregisteredComponentException extends InstantiationException {

  private static final long serialVersionUID = -855088582128419315L;

  UnregisteredComponentException(Class<?> type) {
    super(type);
  }
}
