package ca.ulaval.glo4003.service.stock;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.service.Component;
import ca.ulaval.glo4003.ws.api.stock.StockDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

  public List<StockDto> toDtoList(List<Stock> stocks) {
    return stocks.stream().map(this::toDto).collect(Collectors.toList());
  }
}
