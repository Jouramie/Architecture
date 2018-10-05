package ca.ulaval.glo4003.domain.notification;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionItem;
import ca.ulaval.glo4003.domain.transaction.TransactionType;
import ca.ulaval.glo4003.util.TransactionItemBuilder;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class NotificationFactoryTest {
  private static final TransactionItem AN_ITEM = new TransactionItemBuilder().build();
  private static final TransactionItem ANOTHER_ITEM = new TransactionItemBuilder().build();
  private static final List<TransactionItem> SOME_TRANSACTION_ITEMS
      = Arrays.asList(AN_ITEM, ANOTHER_ITEM);
  private final Transaction transaction
      = new Transaction(LocalDateTime.now(), SOME_TRANSACTION_ITEMS, TransactionType.PURCHASE);

  private final NotificationFactory notificationFactory = new NotificationFactory();

  @Test
  public void whenCreatingTransactionNotification_thenTitleContainsTransactionType() {
    Notification notification = notificationFactory.create(transaction);

    assertThat(notification.title).contains(transaction.type.toString());
  }

  @Test
  public void whenCreatingTransactionNotification_thenMessageContainsStockId() {
    Notification notification = notificationFactory.create(transaction);

    TransactionItem item = transaction.items.get(0);
    assertThat(notification.message).contains(item.title);
  }

  @Test
  public void whenCreatingTransactionNotification_thenMessageContainsStockQuantity() {
    Notification notification = notificationFactory.create(transaction);

    TransactionItem item = transaction.items.get(0);
    assertThat(notification.message).contains(Integer.toString(item.quantity));
  }

  @Test
  public void whenCreatingTransactionNotification_thenMessageContainsStockCost() {
    Notification notification = notificationFactory.create(transaction);

    TransactionItem item = transaction.items.get(0);
    assertThat(notification.message).contains(item.amount.getAmount().toString());
  }

  @Test
  public void whenCreatingTransactionNotification_thenMessageContainsStockCostCurrency() {
    Notification notification = notificationFactory.create(transaction);

    TransactionItem item = transaction.items.get(0);
    assertThat(notification.message).contains(item.amount.getCurrency().getName());
  }

  @Test
  public void whenCreatingTransactionNotification_thenMessageContainsTotal() {
    Notification notification = notificationFactory.create(transaction);

    MoneyAmount total = transaction.calculateTotal();
    assertThat(notification.message).contains(total.getAmount().toString());
  }
}
