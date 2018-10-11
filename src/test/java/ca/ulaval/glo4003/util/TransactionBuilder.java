package ca.ulaval.glo4003.util;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionItem;
import ca.ulaval.glo4003.domain.transaction.TransactionType;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class TransactionBuilder {
  public static final List<TransactionItem> DEFAULT_ITEMS = Collections
      .singletonList(new TransactionItemBuilder().build());

  public static final TransactionType DEFAULT_TYPE = TransactionType.PURCHASE;
  public static final LocalDateTime DEFAULT_TIME
      = LocalDateTime.of(1970, 1, 1, 1, 1, 1);
  private final TransactionType type = DEFAULT_TYPE;
  private final List<TransactionItem> items = DEFAULT_ITEMS;
  private LocalDateTime time = DEFAULT_TIME;

  public Transaction build() {
    return new Transaction(time, items, type);
  }

  public TransactionBuilder withTime(LocalDateTime time) {
    this.time = time;
    return this;
  }
}
