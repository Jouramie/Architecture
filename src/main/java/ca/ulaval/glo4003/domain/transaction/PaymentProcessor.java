package ca.ulaval.glo4003.domain.transaction;

public interface PaymentProcessor {
  void payment(Transaction transaction);
}
