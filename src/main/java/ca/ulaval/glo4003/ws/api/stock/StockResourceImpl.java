package ca.ulaval.glo4003.ws.api.stock;

import ca.ulaval.glo4003.service.stock.StockDto;
import ca.ulaval.glo4003.service.stock.StockService;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;

@Resource
public class StockResourceImpl implements StockResource {

  private final StockService stockService;
  private final ApiStockAssembler apiStockAssembler;


  @Inject
  public StockResourceImpl(StockService stockService, ApiStockAssembler apiStockAssembler) {
    this.stockService = stockService;
    this.apiStockAssembler = apiStockAssembler;
  }

  @Override
  public ApiStockDto getStockByTitle(String title) {
    StockDto stockDto = stockService.getStockByTitle(title);
    return apiStockAssembler.toDto(stockDto);
  }

  @Override
  public List<ApiStockDto> getStocks(String name, String category, int page, int perPage) {
    List<StockDto> stockDtos = stockService.queryStocks(name, category);
    return apiStockAssembler.toDtoList(stockDtos);
  }
}
