package ca.ulaval.glo4003.domain.notification;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionItem;
import java.util.List;

@Component
public class NotificationFactory {

  private static final String TRANSACTION_ENTRY_FORMAT = "%s %sx %s %s : %s%s";

  public Notification create(Transaction transaction) {
    return new Notification(buildNotificationTitle(transaction),
        buildTransactionSummary(transaction));
  }

  private String buildNotificationTitle(Transaction transaction) {
    return transaction.type.toString() + " summary - InvestUL";
  }

  private String buildTransactionSummary(Transaction transaction) {
    List<String> entries = transaction.items.stream()
        .map(this::buildTransactionEntry)
        .collect(toList());
    entries.add(buildTotalEntry(transaction));
    return String.join("\n", entries);
  }

  private String buildTransactionEntry(TransactionItem item) {
    MoneyAmount total = item.amount.multiply(item.quantity);
    return String.format(TRANSACTION_ENTRY_FORMAT, item.title, item.quantity,
        item.amount.getAmount(), item.amount.getCurrency().getName(), total.getAmount(),
        total.getCurrency().getName());
  }

  private String buildTotalEntry(Transaction transaction) {
    return "Total: " + transaction.calculateTotal().getAmount().toString()
        + transaction.calculateTotal().getCurrency().getName();
  }
}
