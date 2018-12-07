package ca.ulaval.glo4003.domain.transaction;

import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionNoName {
  private final UserRepository userRepository;

  public TransactionNoName(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<Transaction> getTransactions(LocalDate from, LocalDate to) {
    List<Transaction> transactions = new ArrayList<>();

    for (Transaction transaction : getAllTransactions()) {
      if (transaction.verifyDatesIsBetween(from, to)) {
        transactions.add(transaction);
      }
    }
    return transactions;
  }

  public List<Transaction> getTransactionsByEmail(String email, LocalDate from, LocalDate to) throws UserNotFoundException {
    Investor user = (Investor) userRepository.findByEmail(email);
    return user.getTransactions();
  }

  public List<Transaction> getTransactionsByTitle(String title, LocalDate from, LocalDate to) {
    List<Transaction> transactions = new ArrayList<>();

    for (Transaction transaction : getAllTransactions()) {
    }
    return null;
  }


  private List<Transaction> getAllTransactions() {
    List<Investor> investors = userRepository.findInvestor();
    List<Transaction> transactions = new ArrayList<>();

    for (Investor investor : investors) {
      transactions.stream().map(transaction -> investor.getTransactions()).collect(Collectors.toList());
    }
    return transactions;
  }
}
