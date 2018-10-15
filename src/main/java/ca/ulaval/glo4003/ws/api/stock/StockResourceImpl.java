package ca.ulaval.glo4003.ws.api.stock;

import ca.ulaval.glo4003.service.stock.StockService;
import ca.ulaval.glo4003.service.stock.max.StockMaxValueSinceParameter;
import ca.ulaval.glo4003.ws.api.stock.max.StockMaxResponseDto;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

@Resource
public class StockResourceImpl implements StockResource {

  private final StockService stockService;

  @Inject
  public StockResourceImpl(StockService stockService) {
    this.stockService = stockService;
  }

  @Override
  public StockDto getStockByTitle(String title) {
    return stockService.getStockByTitle(title);
  }

  @Override
  public StockDto getStockByName(String name) {
    if (name == null || name.isEmpty()) {
      throw new BadRequestException("Missing name query parameter");
    }
    return stockService.getStockByName(name);
  }

  @Override
  public StockMaxResponseDto getStockMaxValue(String title, StockMaxValueSinceParameter since) {
    return stockService.getStockMaxValue(title, since);
  }
}
