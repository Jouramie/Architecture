package ca.ulaval.glo4003.service.stock;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.service.Component;
import java.util.List;
import javax.inject.Inject;

@Component
public class StockService {
  private final StockRepository stockRepository;
  private final StockAssembler stockAssembler;

  @Inject
  public StockService(StockRepository stockRepository,
                      StockAssembler stockAssembler) {
    this.stockRepository = stockRepository;
    this.stockAssembler = stockAssembler;
  }

  public StockDto getStockByTitle(String title) {
    Stock stock = getStockByTitleOrThrowException(title);
    return stockAssembler.toDto(stock);
  }

  private Stock getStockByTitleOrThrowException(String title) {
    try {
      return stockRepository.findByTitle(title);
    } catch (StockNotFoundException exception) {
      throw new StockDoesNotExistException(exception);
    }
  }

  public List<StockDto> queryStocks(String name, String category) {
    return stockAssembler.toDtoList(stockRepository.queryStocks(name, category));
  }

  public List<String> getCategories() {
    return stockRepository.findAllCategories();
  }
}
