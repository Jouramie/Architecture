package ca.ulaval.glo4003.service.stock;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.query.StockQuery;
import ca.ulaval.glo4003.domain.stock.query.StockQueryBuilder;
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
    StockQuery stockQuery = new StockQueryBuilder().withTitle(title).build();
    List<Stock> stocks = stockRepository.find(stockQuery);
    if (stocks.isEmpty()) {
      throw new StockDoesNotExistException(title);
    }
    return stocks.get(0);
  }

  public List<StockDto> queryStocks(String name, String category) {
    StockQueryBuilder stockQueryBuilder = new StockQueryBuilder();
    if (name != null) {
      stockQueryBuilder = stockQueryBuilder.withName(name);
    }
    if (category != null) {
      stockQueryBuilder = stockQueryBuilder.withCategory(category);
    }
    List<Stock> stocks = stockRepository.find(stockQueryBuilder.build());
    return stockAssembler.toDtoList(stocks);
  }

  public List<String> getCategories() {
    return stockRepository.findAllCategories();
  }
}
