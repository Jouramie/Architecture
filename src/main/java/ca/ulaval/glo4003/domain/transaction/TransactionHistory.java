package ca.ulaval.glo4003.domain.transaction;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;

public class TransactionHistory {
  private final TreeSet<Transaction> transactions = new TreeSet<>();

  public void save(Transaction transaction) {
    transactions.add(transaction);
  }

  public TreeSet<Transaction> getTransactions(LocalDate date) {
    return new TreeSet<>(transactions.stream()
        .filter((transaction) -> transaction.timestamp.toLocalDate().isEqual(date))
        .collect(toList()));
  }

  public List<Transaction> getTransactions() {
    return transactions.stream().collect(toList());
  }
}
