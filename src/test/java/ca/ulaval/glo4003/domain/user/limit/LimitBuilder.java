package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.time.LocalDateTime;

public class LimitBuilder {

  public static final LocalDateTime DEFAULT_START = LocalDateTime.MIN;
  public static final LocalDateTime DEFAULT_END = LocalDateTime.MAX;
  public static final int DEFAULT_STOCK_QUANTITY = 91241;
  public static final MoneyAmount DEFAULT_MONEY_AMOUNT = MoneyAmount.ZERO;
  private final MoneyAmount moneyAmount = DEFAULT_MONEY_AMOUNT;
  private LocalDateTime start = DEFAULT_START;
  private LocalDateTime end = DEFAULT_END;
  private int stockQuantity = DEFAULT_STOCK_QUANTITY;

  public LimitBuilder withStart(LocalDateTime start) {
    this.start = start;
    return this;
  }

  public LimitBuilder withEnd(LocalDateTime end) {
    this.end = end;
    return this;
  }

  public LimitBuilder withStockQuantity(int stockQuantity) {
    this.stockQuantity = stockQuantity;
    return this;
  }

  public Limit build() {
    return buildNullLimit();
  }

  public Limit buildNullLimit() {
    return new NullLimit();
  }

  public Limit buildStockQuantityLimit() {
    return new StockQuantityLimit(start, end, stockQuantity);
  }

  public Limit buildMoneyAmountLimit() {
    return new MoneyAmountLimit(start, end, moneyAmount);
  }
}
