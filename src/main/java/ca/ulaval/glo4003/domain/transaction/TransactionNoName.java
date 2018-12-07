package ca.ulaval.glo4003.domain.transaction;

import ca.ulaval.glo4003.domain.user.UserRepository;
import java.time.LocalDate;
import java.util.List;

public class TransactionNoName {
  private final UserRepository userRepository;

  public TransactionNoName(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<Transaction> getTransactions(LocalDate from, LocalDate to) {
    return null;
  }

  public List<Transaction> getTransactionsByEmail(String email, LocalDate from, LocalDate to) {
    //List<Transaction> transactions = userRepository.
    return null;
  }

  public List<Transaction> getTransactionsByTitle(String title, LocalDate from, LocalDate to) {
    return null;
  }

  private List<Transaction> getAllTransactions() {
    return null;
  }
}
