package ca.ulaval.glo4003.domain.transaction;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

public class TransactionHistory {
  private final TreeSet<Transaction> transactions = new TreeSet<>();

  public void save(Transaction transaction) {
    transactions.add(transaction);
  }

  public TreeSet<Transaction> getTransactions(LocalDate date) {
    return transactions.stream()
        .filter((transaction) -> transaction.timestamp.toLocalDate().isEqual(date))
        .collect(toCollection(TreeSet::new));
  }

  public List<Transaction> getTransactions(LocalDateTime from, LocalDateTime to) {
    return transactions.stream()
        .filter(transaction -> transaction.isDateInRange(from, to))
        .collect(toList());
  }
}
