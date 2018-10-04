package ca.ulaval.glo4003.infrastructure.injection;

class NonInjectableConstructorException extends InstantiationException {

  private static final long serialVersionUID = -8451705022159498134L;

  NonInjectableConstructorException(Class<?> type) {
    super(type);
  }
}
