package ca.ulaval.glo4003.domain.notification;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionItem;
import java.util.List;

public class NotificationFactory {
  public Notification create(Transaction transaction) {
    return new Notification(buildNotificationTitle(transaction),
        buildTransactionSummary(transaction));
  }

  private String buildNotificationTitle(Transaction transaction) {
    return transaction.getType().toString() + " summary - InvestUL";
  }

  private String buildTransactionSummary(Transaction transaction) {
    List<String> entries = transaction.getListItems().stream()
        .map(this::buildMessageEntry)
        .collect(toList());
    entries.add(buildTotalEntry(transaction));
    return String.join("\n", entries);
  }

  private String buildMessageEntry(TransactionItem item) {
    MoneyAmount total = item.amount.multiply(item.quantity);
    return item.stockId + " " + item.quantity + "x " + item.amount.getAmount()
        + item.amount.getCurrency().getName() + " " + ": "
        + total.getAmount() + total.getCurrency().getName();
  }

  private String buildTotalEntry(Transaction transaction) {
    return "Total: " + transaction.getTotal().getAmount().toString()
        + transaction.getTotal().getCurrency().getName();
  }
}
