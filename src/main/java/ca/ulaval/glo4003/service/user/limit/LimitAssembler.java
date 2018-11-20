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
      return new StockQuantityLimitDto(limit.start, limit.end, ((StockQuantityLimit) limit).stockQuantity);
    }

    if (limit instanceof MoneyAmountLimit) {
      return new MoneyAmountLimitDto(limit.start, limit.end, ((MoneyAmountLimit) limit).amount.toUsd());
    }

    if (limit instanceof NullLimit) {
      return null;
    }

    throw new UnsupportedOperationException("There is no conversion for this limit: " + limit);
  }
}
