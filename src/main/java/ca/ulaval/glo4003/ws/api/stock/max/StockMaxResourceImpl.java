package ca.ulaval.glo4003.ws.api.stock.max;

import ca.ulaval.glo4003.service.stock.max.StockMaxValueService;
import ca.ulaval.glo4003.service.stock.max.dto.StockMaxValueSummary;
import javax.annotation.Resource;
import javax.inject.Inject;

@Resource
public class StockMaxResourceImpl implements StockMaxResource {

  private final StockMaxValueService stockMaxValueService;
  private final StockMaxResponseDtoAssembler assembler;

  @Inject
  public StockMaxResourceImpl(StockMaxValueService stockMaxValueService, StockMaxResponseDtoAssembler assembler) {
    this.stockMaxValueService = stockMaxValueService;
    this.assembler = assembler;
  }

  @Override
  public StockMaxResponseDto getStockMaxValue(String title) {
    StockMaxValueSummary maxValueSummary = stockMaxValueService.getStockMaxValue(title);
    return assembler.toDto(title, maxValueSummary);
  }
}
