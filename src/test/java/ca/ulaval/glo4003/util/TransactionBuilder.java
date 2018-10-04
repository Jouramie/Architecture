package ca.ulaval.glo4003.util;

import ca.ulaval.glo4003.infrastructure.payment.Transaction;
import ca.ulaval.glo4003.infrastructure.payment.TransactionItem;
import ca.ulaval.glo4003.infrastructure.payment.TransactionType;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TransactionBuilder {
  private static final List<TransactionItem> DEFAULT_ITEMS = Arrays.asList(new TransactionItemBuilder().buildDefault());

  private static final TransactionType DEFAULT_TYPE = TransactionType.PURCHASE;
  private final TransactionType type = DEFAULT_TYPE;
  private final List<TransactionItem> items = DEFAULT_ITEMS;

  public Transaction withTime(LocalDateTime time) {
    return new Transaction(time, items, type);
  }
}
