package ca.ulaval.glo4003.service.stock;

import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.infrastructure.injection.Component;
import ca.ulaval.glo4003.service.stock.max.MaximumValueStockRetriever;
import ca.ulaval.glo4003.service.stock.max.StockMaxResponseAssembler;
import ca.ulaval.glo4003.service.stock.max.StockMaxValueSinceParameter;
import ca.ulaval.glo4003.ws.api.stock.StockDto;
import ca.ulaval.glo4003.ws.api.stock.max.StockMaxResponseDto;
import javax.inject.Inject;

@Component
public class StockService {
  private final StockRepository stockRepository;
  private final StockAssembler stockAssembler;
  private final MaximumValueStockRetriever maximumValueStockRetriever;
  private final StockMaxResponseAssembler stockMaxResponseAssembler;

  @Inject
  public StockService(StockRepository stockRepository,
                      StockAssembler stockAssembler,
                      MaximumValueStockRetriever maximumValueStockRetriever,
                      StockMaxResponseAssembler stockMaxResponseAssembler) {
    this.stockRepository = stockRepository;
    this.stockAssembler = stockAssembler;
    this.maximumValueStockRetriever = maximumValueStockRetriever;
    this.stockMaxResponseAssembler = stockMaxResponseAssembler;
  }

  public StockDto getStockByTitle(String title) {
    Stock stock = getStockByTitleOrThrowException(title);
    return stockAssembler.toDto(stock);
  }

  public StockDto getStockByName(String name) {
    try {
      Stock stock = stockRepository.getByName(name);
      return stockAssembler.toDto(stock);
    } catch (StockNotFoundException exception) {
      throw new StockDoesNotExistException(exception);
    }
  }

  public StockMaxResponseDto getStockMaxValue(String title, StockMaxValueSinceParameter parameter) {
    Stock stock = getStockByTitleOrThrowException(title);
    try {
      HistoricalStockValue maximumValue = maximumValueStockRetriever.getStockMaxValue(stock, parameter);
      return stockMaxResponseAssembler.toDto(title, maximumValue);
    } catch (NoStockValueFitsCriteriaException e) {
      System.out.println("This exception should never happen.");
      e.printStackTrace();
      return null;
    }
  }

  private Stock getStockByTitleOrThrowException(String title) {
    try {
      return stockRepository.getByTitle(title);
    } catch (StockNotFoundException exception) {
      throw new StockDoesNotExistException(exception);
    }
  }
}
