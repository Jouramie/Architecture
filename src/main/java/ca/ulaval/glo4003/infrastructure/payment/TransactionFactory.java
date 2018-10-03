package ca.ulaval.glo4003.infrastructure.payment;

import ca.ulaval.glo4003.domain.clock.Clock;
import java.util.List;

public class TransactionFactory {

  public Transaction create(Clock clock, List<TransactionItem> items, TransactionType type) {
    return new Transaction(clock, items, type);
  }
}
