package ca.ulaval.glo4003.ws.api.user.assemblers;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.service.user.limit.dto.MoneyAmountLimitDto;
import ca.ulaval.glo4003.service.user.limit.dto.StockLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserMoneyAmountLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserStockLimitDto;

@Component
public class ApiUserLimitAssembler {

  public ApiUserLimitDto toDtoMoneyAmountLimit(MoneyAmountLimitDto moneyAmountLimitDto) {
    return new ApiUserMoneyAmountLimitDto(moneyAmountLimitDto.maximalMoneySpent, moneyAmountLimitDto.beginDate, moneyAmountLimitDto.endDate);
  }

  public ApiUserLimitDto toDtoStockLimit(StockLimitDto stockLimitDto) {
    return new ApiUserStockLimitDto(stockLimitDto.maximalStockQuantity, stockLimitDto.beginDate, stockLimitDto.endDate);
  }
}
