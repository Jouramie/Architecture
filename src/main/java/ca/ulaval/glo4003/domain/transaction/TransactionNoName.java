package ca.ulaval.glo4003.domain.transaction;

import ca.ulaval.glo4003.domain.user.UserRepository;
import java.time.LocalDate;
import java.util.List;

public class TransactionNoName {
  private final UserRepository userRepository;
  public TransactionNoName(UserRepository userRepository){
    this.userRepository = userRepository;
  }

/*  public List<Transaction> getTransactionByEmail(LocalDate from, LocalDate to){
    List<Transaction> transactions = userRepository.
  }
  public List<Transaction> getTransactionByTitle(LocalDate from, LocalDate to){

  }
  public List<Transaction> getAllTransaction(LocalDate from, LocalDate to){

  }*/
}
