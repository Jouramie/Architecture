package ca.ulaval.glo4003.infrastructure.payment;

public class NullPaymentProcessor implements PaymentProcessor {

  @Override
  public void payment(Transaction transaction) {
  }
}
