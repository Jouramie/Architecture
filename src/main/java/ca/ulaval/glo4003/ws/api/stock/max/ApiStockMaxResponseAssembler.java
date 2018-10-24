package ca.ulaval.glo4003.ws.api.stock.max;

import ca.ulaval.glo4003.service.Component;
import ca.ulaval.glo4003.service.stock.max.StockMaxResponseDto;

@Component
public class ApiStockMaxResponseAssembler {
  public ApiStockMaxResponseDto toDto(StockMaxResponseDto stockMaxResponseDto) {
    return new ApiStockMaxResponseDto(
        stockMaxResponseDto.title,
        stockMaxResponseDto.maximumValue,
        stockMaxResponseDto.maximumValueDate);
  }
}
