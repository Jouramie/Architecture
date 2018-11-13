package ca.ulaval.glo4003.domain.transaction;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class TransactionHistory {
  private final SortedSet<Transaction> transactions = new TreeSet<>();

  public void save(Transaction transaction) {
    transactions.add(transaction);
  }

  public SortedSet<Transaction> getTransactions(LocalDate from, LocalDate to) {
    List<Transaction> filterTransactions = transactions.stream()
        .filter((transaction) -> checkIfWithinDates(transaction.timestamp, from, to))
        .collect(toList());

    return new TreeSet<>(filterTransactions);
  }

  private boolean checkIfWithinDates(LocalDateTime timestamp, LocalDate from, LocalDate to) {
    LocalDateTime fromDateTime = from.atStartOfDay();
    LocalDateTime toDateTime = to.atTime(LocalTime.MAX);
    return (timestamp.isEqual(fromDateTime) || timestamp.isAfter(fromDateTime)) &&
        (timestamp.isEqual(toDateTime) || timestamp.isBefore(toDateTime));
  }
}
