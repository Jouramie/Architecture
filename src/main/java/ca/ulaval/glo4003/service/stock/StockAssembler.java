package ca.ulaval.glo4003.service.stock;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.stock.Stock;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockAssembler {
  public StockDto toDto(Stock stock) {
    BigDecimal closeValue = null;
    if (stock.isClosed()) {
      closeValue = stock.getValue().getLatestValue().toUsd();
    }

    return new StockDto(
        stock.getTitle(),
        stock.getName(),
        stock.getCategory(),
        stock.getMarketId().getValue(),
        stock.getValue().getOpenValue().toUsd(),
        stock.getValue().getLatestValue().toUsd(),
        closeValue);
  }

  public List<StockDto> toDtoList(List<Stock> stocks) {
    return stocks.stream().map(this::toDto).collect(Collectors.toList());
  }
}
