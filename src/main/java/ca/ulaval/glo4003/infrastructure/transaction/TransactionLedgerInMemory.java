package ca.ulaval.glo4003.infrastructure.transaction;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionLedger;
import java.util.ArrayList;
import java.util.List;

public class TransactionLedgerInMemory implements TransactionLedger {
  private final List<Transaction> transactions;

  public TransactionLedgerInMemory() {
    transactions = new ArrayList<>();
  }

  @Override
  public void save(Transaction transaction) {
    transactions.add(transaction);
  }

  @Override
  public List<Transaction> getTransactions() {
    return transactions;
  }
}
