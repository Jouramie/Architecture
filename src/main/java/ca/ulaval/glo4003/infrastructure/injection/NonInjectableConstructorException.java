package ca.ulaval.glo4003.infrastructure.injection;

class NonInjectableConstructorException extends InstantiationException {

  private static final long serialVersionUID = 5258243115652838203L;

  NonInjectableConstructorException(Class<?> type) {
    super(type);
  }
}
