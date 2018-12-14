package ca.ulaval.glo4003.domain.stock.exception;

public class NoStockValueFitsCriteriaException extends Exception {
  public NoStockValueFitsCriteriaException() {
    super("No stock value fits criteria.");
  }
}
