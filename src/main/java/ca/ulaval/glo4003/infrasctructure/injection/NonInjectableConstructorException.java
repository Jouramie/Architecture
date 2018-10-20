package ca.ulaval.glo4003.infrasctructure.injection;

class NonInjectableConstructorException extends InstantiationException {

  NonInjectableConstructorException(Class<?> type) {
    super(type);
  }
}
