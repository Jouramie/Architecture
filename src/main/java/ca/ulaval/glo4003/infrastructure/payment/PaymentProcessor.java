package ca.ulaval.glo4003.infrastructure.payment;

public interface PaymentProcessor {
  void payment(Transaction transaction);
}
