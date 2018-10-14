package ca.ulaval.glo4003.service.stock;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.infrastructure.injection.Component;
import ca.ulaval.glo4003.ws.api.stock.StockDto;
import java.math.BigDecimal;

@Component
public class StockAssembler {
  public StockDto toDto(Stock stock) {
    MoneyAmount closeMoneyAmount = stock.getValue().getCloseValue();
    BigDecimal closeValue = null;
    if (closeMoneyAmount != null) {
      closeValue = closeMoneyAmount.toUsd();
    }

    return new StockDto(
        stock.getTitle(),
        stock.getName(),
        stock.getCategory(),
        stock.getMarketId().getValue(),
        stock.getValue().getOpenValue().toUsd(),
        stock.getValue().getCurrentValue().toUsd(),
        closeValue);
  }
}
