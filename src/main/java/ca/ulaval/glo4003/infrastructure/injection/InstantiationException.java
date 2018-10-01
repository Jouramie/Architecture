package ca.ulaval.glo4003.infrastructure.injection;

class InstantiationException extends RuntimeException {

  private static final long serialVersionUID = 6279385361862887881L;

  InstantiationException(Class<?> type) {
    super(String.format("Cannot instantiate object of class %s.", type.toString()));
  }
}
