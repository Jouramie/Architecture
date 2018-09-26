package ca.ulaval.glo4003.ws.api.stock;

import ca.ulaval.glo4003.service.stock.StockDto;
import ca.ulaval.glo4003.service.stock.StockService;

public class StockResourceImpl implements StockResource {

  private final StockService stockService;

  public StockResourceImpl(StockService stockService) {
    this.stockService = stockService;
  }

  @Override
  public StockDto getStockByTitle(String title) {
    return stockService.getStockByTitle(title);
  }

  @Override
  public StockDto getStockByName(String name) {
    return stockService.getStockByName(name);
  }
}
