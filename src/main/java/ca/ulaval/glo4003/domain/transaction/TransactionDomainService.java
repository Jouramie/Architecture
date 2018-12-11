package ca.ulaval.glo4003.domain.transaction;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@Component
public class TransactionDomainService {
  private final UserRepository userRepository;

  @Inject
  public TransactionDomainService(UserRepository userRepository) {
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
    List<Transaction> transactions = new ArrayList<>();

    for (Transaction transaction : user.getTransactions()) {
      if (transaction.verifyDatesIsBetween(from, to)) {
        transactions.add(transaction);
      }
    }
    return transactions;
  }

  public List<Transaction> getTransactionsByTitle(String title, LocalDate from, LocalDate to) {
    return getAllTransactions().stream().filter(transaction -> transaction.verifyDatesIsBetween(from, to) && transaction.doContainTitle(title)).collect(toList());
  }

  private List<Transaction> getAllTransactions() {
    List<Investor> investors = userRepository.findInvestor();
    return investors.stream().flatMap(investor -> investor.getTransactions().stream()).collect(toList());
  }
}
