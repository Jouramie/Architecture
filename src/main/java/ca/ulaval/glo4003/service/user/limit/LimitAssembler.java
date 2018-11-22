package ca.ulaval.glo4003.service.user.limit;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.service.user.limit.dto.MoneyAmountLimitDto;
import ca.ulaval.glo4003.service.user.limit.dto.StockLimitDto;
import java.time.LocalDateTime;

@Component
public class LimitAssembler {

  public MoneyAmountLimitDto toDtoMoneyAmountLimit(double amount, LocalDateTime beginDateTime, LocalDateTime endDateTime) {
    return new MoneyAmountLimitDto(amount, beginDateTime, endDateTime);
  }

  public StockLimitDto toDtoStockLimit(int maximalQuantity, LocalDateTime beginDateTime, LocalDateTime endDateTime) {
    return new StockLimitDto(maximalQuantity, beginDateTime, endDateTime);
  }
}