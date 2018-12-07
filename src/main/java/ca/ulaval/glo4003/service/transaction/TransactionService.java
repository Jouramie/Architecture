package ca.ulaval.glo4003.service.transaction;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionNoName;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import ca.ulaval.glo4003.service.cart.assemblers.TransactionAssembler;
import ca.ulaval.glo4003.service.cart.dto.TransactionDto;
import ca.ulaval.glo4003.service.date.DateService;
import ca.ulaval.glo4003.service.date.SinceParameter;
import ca.ulaval.glo4003.service.user.UserDoesNotExistException;
import java.time.LocalDate;
import java.util.List;

public class TransactionService {

  private final TransactionAssembler transactionAssembler;
  private final TransactionNoName transactionNoName;
  private final DateService dateService;

  public TransactionService(TransactionAssembler transactionAssembler,
                            TransactionNoName transactionNoName,
                            DateService dateService) {
    this.transactionAssembler = transactionAssembler;
    this.transactionNoName = transactionNoName;
    this.dateService = dateService;
  }

  public List<TransactionDto> getAllTransactions(SinceParameter since) {
    LocalDate from = dateService.getDateSince(since);
    LocalDate to = dateService.getCurrentDate();
    List<Transaction> transactions = transactionNoName.getTransactions(from, to);
    return transactionAssembler.toDtosList(transactions);
  }

  public List<TransactionDto> getTransactionsByEmail(String email, SinceParameter since) {
    LocalDate from = dateService.getDateSince(since);
    LocalDate to = dateService.getCurrentDate();
    try {
      List<Transaction> transactions = transactionNoName.getTransactionsByEmail(email, from, to);
      return transactionAssembler.toDtosList(transactions);
    } catch (UserNotFoundException e) {
      throw new UserDoesNotExistException(e);
    }
  }

  public List<TransactionDto> getTransactionsByTitle(String title, SinceParameter since) {
    LocalDate from = dateService.getDateSince(since);
    LocalDate to = dateService.getCurrentDate();
    List<Transaction> transactions = transactionNoName.getTransactionsByTitle(title, from, to);
    return transactionAssembler.toDtosList(transactions);
  }
}
