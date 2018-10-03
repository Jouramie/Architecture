package ca.ulaval.glo4003.infrastructure.payment;

import ca.ulaval.glo4003.domain.clock.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
  private final TransactionType type;
  private final List<TransactionItem> items;
  private LocalDateTime timestamp;

  public Transaction(Clock clock, List<TransactionItem> items, TransactionType type) {
    this.type = type;
    this.items = new ArrayList<>(items);
    timestamp = clock.getCurrentTime();
  }

  public LocalDateTime getTime() {
    return timestamp;
  }

  public void setTime(LocalDateTime time) {
    timestamp = time;
  }

  public TransactionType getType() {
    return type;
  }

  public List<TransactionItem> getListItems() {
    return items;
  }
}
