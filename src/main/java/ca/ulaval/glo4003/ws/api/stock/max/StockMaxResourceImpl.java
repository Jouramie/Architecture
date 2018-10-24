package ca.ulaval.glo4003.ws.api.stock.max;

import ca.ulaval.glo4003.service.stock.StockService;
import ca.ulaval.glo4003.service.stock.max.StockMaxResponseDto;
import ca.ulaval.glo4003.service.stock.max.StockMaxValueSinceRange;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

@Resource
public class StockMaxResourceImpl implements StockMaxResource {

  private final StockService stockService;
  private final ApiStockMaxResponseAssembler apiStockMaxResponseAssembler;

  @Inject
  public StockMaxResourceImpl(StockService stockService, ApiStockMaxResponseAssembler apiStockMaxResponseAssembler) {
    this.stockService = stockService;
    this.apiStockMaxResponseAssembler = apiStockMaxResponseAssembler;
  }

  @Override
  public ApiStockMaxResponseDto getStockMaxValue(String title, String since) {
    if (since == null) {
      throw new BadRequestException("Missing 'since' query parameter.");
    }

    try {
      StockMaxValueSinceRange sinceParameter = StockMaxValueSinceRange.valueOf(since);
      StockMaxResponseDto maxResponseDto = stockService.getStockMaxValue(title, sinceParameter);
      return apiStockMaxResponseAssembler.toDto(maxResponseDto);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid 'since' query parameter");
    }
  }
}
