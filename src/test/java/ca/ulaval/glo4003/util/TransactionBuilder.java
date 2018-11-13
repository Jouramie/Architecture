package ca.ulaval.glo4003.util;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionItem;
import ca.ulaval.glo4003.domain.transaction.TransactionType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionBuilder {
  public static final List<TransactionItem> DEFAULT_ITEMS = Collections
      .singletonList(new TransactionItemBuilder().build());

  public static final TransactionType DEFAULT_TYPE = TransactionType.PURCHASE;
  public static final LocalDateTime DEFAULT_TIME
      = LocalDateTime.of(1970, 1, 1, 1, 1, 1);
  private TransactionType type = DEFAULT_TYPE;
  private List<TransactionItem> items = new ArrayList<>();
  private LocalDateTime time = DEFAULT_TIME;

  public Transaction build() {
    return new Transaction(time, items, type);
  }

  public TransactionBuilder ofType(TransactionType type) {
    this.type = type;
    return this;
  }

  public TransactionBuilder withTime(LocalDateTime time) {
    this.time = time;
    return this;
  }

  public TransactionBuilder withItem(TransactionItem item) {
    items.add(item);
    return this;
  }

  public TransactionBuilder withDefaultItems() {
    items = DEFAULT_ITEMS;
    return this;
  }
}
