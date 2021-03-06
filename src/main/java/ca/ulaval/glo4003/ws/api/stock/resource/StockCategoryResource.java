package ca.ulaval.glo4003.ws.api.stock.resource;

import ca.ulaval.glo4003.service.stock.StockService;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/stock_categories")
@Produces(MediaType.APPLICATION_JSON)
@Resource
public class StockCategoryResource implements DocumentedStockCategoryResource {

  private final StockService stockService;

  @Inject
  public StockCategoryResource(StockService stockService) {
    this.stockService = stockService;
  }

  @GET
  @Override
  public List<String> getCategories() {
    return stockService.getCategories();
  }
}
