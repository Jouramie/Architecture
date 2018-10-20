package ca.ulaval.glo4003.infrasctructure.persistence;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionLedger;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTransactionLedger implements TransactionLedger {
  private final List<Transaction> transactions;

  public InMemoryTransactionLedger() {
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
