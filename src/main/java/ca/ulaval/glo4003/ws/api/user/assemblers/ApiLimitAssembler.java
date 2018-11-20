package ca.ulaval.glo4003.ws.api.user.assemblers;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.service.user.limit.LimitDto;
import ca.ulaval.glo4003.service.user.limit.MoneyAmountLimitDto;
import ca.ulaval.glo4003.service.user.limit.StockLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiMoneyAmountLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiStockLimitDto;

@Component
public class ApiLimitAssembler {

  public ApiLimitDto toDto(LimitDto limit) {
    if (limit == null) {
      return null;
    }

    if (limit instanceof StockLimitDto) {
      return new ApiStockLimitDto(limit.from, limit.to, ((StockLimitDto) limit).stockQuantity);
    }

    if (limit instanceof MoneyAmountLimitDto) {
      return new ApiMoneyAmountLimitDto(limit.from, limit.to, ((MoneyAmountLimitDto) limit).moneyAmount);
    }

    throw new UnsupportedOperationException("There is no conversion for this limit: " + limit);
  }
}
