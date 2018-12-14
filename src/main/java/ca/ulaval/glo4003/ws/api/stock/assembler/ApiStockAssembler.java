package ca.ulaval.glo4003.ws.api.stock.assembler;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.service.stock.StockDto;
import ca.ulaval.glo4003.ws.api.stock.dto.ApiStockDto;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApiStockAssembler {

  public ApiStockDto toDto(StockDto stockDto) {
    return new ApiStockDto(stockDto.title, stockDto.name, stockDto.category,
        stockDto.market, stockDto.openValue, stockDto.currentValue, stockDto.closeValue);
  }

  public List<ApiStockDto> toDtoList(List<StockDto> stockDtos) {
    return stockDtos.stream().map(this::toDto).collect(Collectors.toList());
  }
}
