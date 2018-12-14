package ca.ulaval.glo4003.infrastructure.injection.exception;

public class InjectionInstantiationException extends RuntimeException {

  public InjectionInstantiationException(Class<?> type) {
    super(String.format("Cannot instantiate object of class %s.", type.toString()));
  }
}
