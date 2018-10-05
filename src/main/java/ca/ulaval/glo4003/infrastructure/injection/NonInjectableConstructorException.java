package ca.ulaval.glo4003.infrastructure.injection;

class NonInjectableConstructorException extends InstantiationException {

  NonInjectableConstructorException(Class<?> type) {
    super(type);
  }
}
