package ca.ulaval.glo4003.ws.infrastructure.injection;

class UnregisteredComponentException extends InstantiationException {

  private static final long serialVersionUID = 5072814764867007735L;

  UnregisteredComponentException(Class<?> type) {
    super(type);
  }
}
