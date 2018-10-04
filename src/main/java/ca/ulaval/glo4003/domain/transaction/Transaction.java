package ca.ulaval.glo4003.domain.transaction;

import java.time.LocalDateTime;
import java.util.List;

public class Transaction {
  private final TransactionType type;
  private final List<TransactionItem> items;
  private final LocalDateTime timestamp;

  public Transaction(LocalDateTime timestamp, List<TransactionItem> items, TransactionType type) {
    this.type = type;
    this.items = items;
    this.timestamp = timestamp;
  }

  public LocalDateTime getTime() {
    return timestamp;
  }

  public TransactionType getType() {
    return type;
  }

  public List<TransactionItem> getListItems() {
    return items;
  }
}
