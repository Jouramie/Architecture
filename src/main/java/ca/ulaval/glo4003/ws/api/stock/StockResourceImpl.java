package ca.ulaval.glo4003.ws.api.stock;

import ca.ulaval.glo4003.service.stock.StockService;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;

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
  public List<StockDto> getStocks(String name, String category, int page, int perPage) {
    return stockService.queryStocks(name, category);
  }
}
