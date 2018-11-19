package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import java.time.LocalDateTime;

public class NullLimit extends Limit {

  public NullLimit(LocalDateTime start, LocalDateTime end) {
    super(start, end);
  }

  @Override
  public boolean canProcessTransaction(Transaction transaction) {
    return true;
  }
}
