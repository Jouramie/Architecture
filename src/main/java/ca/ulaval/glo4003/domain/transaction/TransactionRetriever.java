package ca.ulaval.glo4003.domain.transaction;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.exception.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.exception.WrongRoleException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import javax.inject.Inject;

@Component
public class TransactionRetriever {
  private final UserRepository userRepository;

  @Inject
  public TransactionRetriever(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<Transaction> getTransactions(LocalDate from, LocalDate to) {
    return getAllTransactions(from, to).collect(toList());
  }

  public List<Transaction> getTransactionsByEmail(String email, LocalDate from, LocalDate to)
      throws UserNotFoundException, WrongRoleException {
    Investor investor = userRepository.findByEmail(email, Investor.class);
    return investor.getTransactions(from.atStartOfDay(), to.atTime(LocalTime.MAX));
  }

  public List<Transaction> getTransactionsByTitle(String title, LocalDate from, LocalDate to) {
    return getAllTransactions(from, to)
        .filter(transaction -> transaction.containsTitle(title))
        .collect(toList());
  }

  private Stream<Transaction> getAllTransactions(LocalDate from, LocalDate to) {
    List<Investor> investors = userRepository.findInvestors();
    return investors.stream()
        .flatMap(investor -> investor.getTransactions(from.atStartOfDay(), to.atTime(LocalTime.MAX)).stream());
  }
}
