package ca.ulaval.glo4003.ws.api.stock;

import ca.ulaval.glo4003.service.stock.StockDto;
import java.util.List;
import java.util.stream.Collectors;

public class ApiStockAssembler {

  public ApiStockDto toDto(StockDto stockDto) {
    return new ApiStockDto(stockDto.title, stockDto.name, stockDto.category,
        stockDto.market, stockDto.openValue, stockDto.currentValue, stockDto.closeValue);
  }

  public List<ApiStockDto> toDtoList(List<StockDto> stocks) {
    return stocks.stream().map(this::toDto).collect(Collectors.toList());
  }
}
