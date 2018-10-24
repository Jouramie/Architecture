package ca.ulaval.glo4003.service.stock;

import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.service.Component;
import ca.ulaval.glo4003.service.InternalErrorException;
import ca.ulaval.glo4003.service.stock.max.StockMaxResponseAssembler;
import ca.ulaval.glo4003.service.stock.max.StockMaxValueRetriever;
import ca.ulaval.glo4003.service.stock.max.StockMaxValueSinceRange;
import ca.ulaval.glo4003.ws.api.stock.max.StockMaxResponseDto;
import java.util.List;
import javax.inject.Inject;

@Component
public class StockService {
  private final StockRepository stockRepository;
  private final StockAssembler stockAssembler;
  private final StockMaxValueRetriever stockMaxValueRetriever;
  private final StockMaxResponseAssembler stockMaxResponseAssembler;

  @Inject
  public StockService(StockRepository stockRepository,
                      StockAssembler stockAssembler,
                      StockMaxValueRetriever stockMaxValueRetriever,
                      StockMaxResponseAssembler stockMaxResponseAssembler) {
    this.stockRepository = stockRepository;
    this.stockAssembler = stockAssembler;
    this.stockMaxValueRetriever = stockMaxValueRetriever;
    this.stockMaxResponseAssembler = stockMaxResponseAssembler;
  }

  public StockDto getStockByTitle(String title) {
    Stock stock = getStockByTitleOrThrowException(title);
    return stockAssembler.toDto(stock);
  }

  public StockMaxResponseDto getStockMaxValue(String title, StockMaxValueSinceRange parameter) {
    Stock stock = getStockByTitleOrThrowException(title);
    try {
      HistoricalStockValue maximumValue = stockMaxValueRetriever.getStockMaxValue(stock, parameter);
      return stockMaxResponseAssembler.toDto(title, maximumValue);
    } catch (NoStockValueFitsCriteriaException e) {
      throw new InternalErrorException("No stock value fits criteria.");
    }
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
