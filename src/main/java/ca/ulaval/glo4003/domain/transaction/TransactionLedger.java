package ca.ulaval.glo4003.domain.transaction;

import java.util.List;

public interface TransactionLedger {
  void save(Transaction transaction);
  
  List<Transaction> getTransactions();
}
