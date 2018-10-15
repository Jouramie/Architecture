package ca.ulaval.glo4003.service.stock.max;

import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.ws.api.stock.max.StockMaxResponseDto;
import javax.annotation.Resource;

@Resource
public class StockMaxResponseAssembler {
  public StockMaxResponseDto toDto(String title, HistoricalStockValue historicalStockValue) {
    return new StockMaxResponseDto(
        title,
        historicalStockValue.value.getMaximumValue().toUsd(),
        historicalStockValue.date);
  }
}
