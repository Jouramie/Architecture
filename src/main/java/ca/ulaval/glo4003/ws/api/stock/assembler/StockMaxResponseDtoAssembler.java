package ca.ulaval.glo4003.ws.api.stock.assembler;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.service.stock.max.StockMaxValueSummary;
import ca.ulaval.glo4003.ws.api.stock.dto.StockMaxResponseDto;
import ca.ulaval.glo4003.ws.api.stock.dto.StockMaxResponseValueDto;

@Component
public class StockMaxResponseDtoAssembler {
  public StockMaxResponseDto toDto(String title, StockMaxValueSummary summary) {
    return new StockMaxResponseDto(
        title,
        toDto(summary.lastFiveDays),
        toDto(summary.currentMonth),
        toDto(summary.lastMonth),
        toDto(summary.lastYear),
        toDto(summary.lastFiveYears),
        toDto(summary.lastTenYears),
        toDto(summary.allTime)
    );
  }

  public StockMaxResponseValueDto toDto(HistoricalStockValue historicalValue) {
    return new StockMaxResponseValueDto(historicalValue.value.getMaximumValue().toUsd(),
        historicalValue.date);
  }
}
