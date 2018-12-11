package ca.ulaval.glo4003.service.transaction;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionRetriever;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.exceptions.WrongRoleException;
import ca.ulaval.glo4003.service.cart.assemblers.TransactionAssembler;
import ca.ulaval.glo4003.service.cart.dto.TransactionDto;
import ca.ulaval.glo4003.service.date.DateService;
import ca.ulaval.glo4003.service.date.SinceParameter;
import ca.ulaval.glo4003.service.stock.StockDoesNotExistException;
import ca.ulaval.glo4003.service.user.UserDoesNotExistException;
import java.time.LocalDate;
import java.util.List;
import javax.inject.Inject;

@Component
public class TransactionService {

  private final TransactionAssembler transactionAssembler;
  private final TransactionRetriever transactionRetriever;
  private final DateService dateService;
  private final StockRepository stockRepository;

  @Inject
  public TransactionService(TransactionAssembler transactionAssembler,
                            TransactionRetriever transactionRetriever,
                            DateService dateService,
                            StockRepository stockRepository) {
    this.transactionAssembler = transactionAssembler;
    this.transactionRetriever = transactionRetriever;
    this.dateService = dateService;
    this.stockRepository = stockRepository;
  }

  public List<TransactionDto> getAllTransactions(SinceParameter since) {
    LocalDate from = dateService.getDateSince(since);
    LocalDate to = dateService.getCurrentDate();
    List<Transaction> transactions = transactionRetriever.getTransactions(from, to);
    return transactionAssembler.toDtoList(transactions);
  }

  public List<TransactionDto> getTransactionsByEmail(String email, SinceParameter since) {
    LocalDate from = dateService.getDateSince(since);
    LocalDate to = dateService.getCurrentDate();
    try {
      List<Transaction> transactions = transactionRetriever.getTransactionsByEmail(email, from, to);
      return transactionAssembler.toDtoList(transactions);
    } catch (UserNotFoundException | WrongRoleException e) {
      throw new UserDoesNotExistException(e);
    }
  }

  public List<TransactionDto> getTransactionsByTitle(String title, SinceParameter since) {
    ensureStockExist(title);
    LocalDate from = dateService.getDateSince(since);
    LocalDate to = dateService.getCurrentDate();
    List<Transaction> transactions = transactionRetriever.getTransactionsByTitle(title, from, to);
    return transactionAssembler.toDtoList(transactions);
  }

  private void ensureStockExist(String title) {
    if (!stockRepository.exists(title)) {
      throw new StockDoesNotExistException();
    }
  }
}
