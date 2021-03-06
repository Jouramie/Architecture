package ca.ulaval.glo4003.domain.transaction;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.time.LocalDateTime;
import java.util.List;

public class Transaction implements Comparable<Transaction> {
  public final TransactionType type;
  public final List<TransactionItem> items;
  public final LocalDateTime timestamp;

  public Transaction(LocalDateTime timestamp, List<TransactionItem> items, TransactionType type) {
    this.type = type;
    this.items = items;
    this.timestamp = timestamp;
  }

  public MoneyAmount calculateTotal() {
    return items.stream()
        .map(TransactionItem::calculateTotal)
        .reduce(MoneyAmount.zero(items.get(0).amount.getCurrency()), MoneyAmount::add);
  }

  public int getTotalQuantity() {
    return items.stream().mapToInt(item -> item.quantity).sum();
  }

  @Override
  public int compareTo(Transaction other) {
    return timestamp.compareTo(other.timestamp);
  }

  public boolean isDateInRange(LocalDateTime from, LocalDateTime to) {
    return timestamp.isEqual(from)
        || timestamp.isEqual(to)
        || (timestamp.isAfter(from)
        && timestamp.isBefore(to));
  }

  public boolean containsTitle(String title) {
    return items.stream().anyMatch(item -> item.title.equals(title));
  }
}
