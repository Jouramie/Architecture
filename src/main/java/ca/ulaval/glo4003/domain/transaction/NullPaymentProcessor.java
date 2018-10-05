package ca.ulaval.glo4003.domain.transaction;

public class NullPaymentProcessor implements PaymentProcessor {

  @Override
  public void payment(Transaction transaction) {
  }
}
