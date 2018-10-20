package ca.ulaval.glo4003.infrasctructure.injection;

class InstantiationException extends RuntimeException {

  InstantiationException(Class<?> type) {
    super(String.format("Cannot instantiate object of class %s.", type.toString()));
  }
}
