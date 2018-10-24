package ca.ulaval.glo4003.ws.api.stock.max;

import ca.ulaval.glo4003.service.stock.StockService;
import ca.ulaval.glo4003.service.stock.max.StockMaxValueSinceRange;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

@Resource
public class StockMaxResourceImpl implements StockMaxResource {

  private final StockService stockService;

  @Inject
  public StockMaxResourceImpl(StockService stockService) {
    this.stockService = stockService;
  }

  @Override
  public StockMaxResponseDto getStockMaxValue(String title, String since) {
    if (since == null) {
      throw new BadRequestException("Missing 'since' query parameter.");
    }

    try {
      StockMaxValueSinceRange sinceParameter = StockMaxValueSinceRange.valueOf(since);
      return stockService.getStockMaxValue(title, sinceParameter);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid 'since' query parameter");
    }
  }
}
