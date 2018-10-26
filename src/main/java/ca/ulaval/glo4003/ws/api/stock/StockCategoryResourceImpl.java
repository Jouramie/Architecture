package ca.ulaval.glo4003.ws.api.stock;

import ca.ulaval.glo4003.service.stock.StockService;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;

@Resource
public class StockCategoryResourceImpl implements StockCategoryResource {

  private final StockService stockService;

  @Inject
  public StockCategoryResourceImpl(StockService stockService) {
    this.stockService = stockService;
  }

  @Override
  public List<String> getCategories() {
    return stockService.getCategories();
  }
}
