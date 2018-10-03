package ca.ulaval.glo4003.util;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.infrastructure.payment.Transaction;
import ca.ulaval.glo4003.infrastructure.payment.TransactionItem;
import ca.ulaval.glo4003.infrastructure.payment.TransactionType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransactionBuilder {
  private static final List<TransactionItem> DEFAULT_ITEMS = Arrays.asList(new TransactionItemBuilder().buildDefault());

  private static final TransactionType DEFAULT_TYPE = TransactionType.PURCHASE;
  private TransactionType type = DEFAULT_TYPE;
  private List<TransactionItem> items = DEFAULT_ITEMS;

  public TransactionBuilder withType(TransactionType type) {
    this.type = type;
    return this;
  }

  public TransactionBuilder withItems(List<TransactionItem> items) {
    this.items = new ArrayList<>(items);
    return this;
  }

  public Transaction build(Clock clock) {
    return new Transaction(clock, items, type);
  }
}
