package ca.ulaval.glo4003.ws.api.stock.assemblers;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.service.stock.max.dto.StockMaxValueSummary;
import ca.ulaval.glo4003.ws.api.stock.dtos.StockMaxResponseDto;
import ca.ulaval.glo4003.ws.api.stock.dtos.StockMaxResponseValueDto;

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
