package ca.ulaval.glo4003.service.user.limit;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.user.limit.Limit;
import ca.ulaval.glo4003.domain.user.limit.MoneyAmountLimit;
import ca.ulaval.glo4003.domain.user.limit.NullLimit;
import ca.ulaval.glo4003.domain.user.limit.StockQuantityLimit;

@Component
public class LimitAssembler {
  public LimitDto toDto(Limit limit) {
    if (limit instanceof StockQuantityLimit) {
      return toStockQuantityLimitDto((StockQuantityLimit) limit);
    }

    if (limit instanceof MoneyAmountLimit) {
      return toMoneyAmountLimitDto((MoneyAmountLimit) limit);
    }

    if (limit instanceof NullLimit) {
      return null;
    }

    throw new UnsupportedOperationException("There is no conversion for this limit: " + limit);
  }

  private StockLimitDto toStockQuantityLimitDto(StockQuantityLimit limit) {
    return new StockLimitDto(limit.start, limit.end, limit.stockQuantity);
  }

  private MoneyAmountLimitDto toMoneyAmountLimitDto(MoneyAmountLimit limit) {
    return new MoneyAmountLimitDto(limit.start, limit.end, limit.amount.toUsd());
  }
}
