package ca.ulaval.glo4003.infrastructure.injection.exception;

public class NonInjectableConstructorException extends InjectionInstantiationException {

  public NonInjectableConstructorException(Class<?> type) {
    super(type);
  }
}
