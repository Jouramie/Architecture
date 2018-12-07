package ca.ulaval.glo4003.service.transaction;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionNoName;
import ca.ulaval.glo4003.service.cart.assemblers.TransactionAssembler;
import ca.ulaval.glo4003.service.cart.dto.TransactionDto;
import ca.ulaval.glo4003.service.date.SinceParameter;
import java.time.LocalDate;
import java.util.List;

public class TransactionService {

  private final TransactionAssembler transactionAssembler;
  private final TransactionNoName transactionNoName;

  public TransactionService(TransactionAssembler transactionAssembler, TransactionNoName transactionNoName) {
    this.transactionAssembler = transactionAssembler;
    this.transactionNoName = transactionNoName;
  }

  public List<TransactionDto> getAllTransactions(SinceParameter since) {
    List<Transaction> transactions = transactionNoName.getTransactions(LocalDate.now(), LocalDate.now());
    return transactionAssembler.toDtosList(transactions);
  }
}
