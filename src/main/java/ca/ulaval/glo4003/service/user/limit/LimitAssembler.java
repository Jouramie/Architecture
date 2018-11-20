package ca.ulaval.glo4003.service.user.limit;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.user.limit.Limit;
import ca.ulaval.glo4003.domain.user.limit.StockQuantityLimit;

@Component
public class LimitAssembler {
  public LimitDto toDto(Limit limit) {
    if (limit instanceof StockQuantityLimit) {
      return new StockQuantityLimitDto(limit.start, limit.end, ((StockQuantityLimit) limit).stockQuantity);
    }

    return null;
  }
}
