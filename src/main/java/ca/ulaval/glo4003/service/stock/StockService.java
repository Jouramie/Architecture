package ca.ulaval.glo4003.service.stock;

import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.infrastructure.injection.Component;
import ca.ulaval.glo4003.service.stock.max.StockMaxResponseAssembler;
import ca.ulaval.glo4003.service.stock.max.StockMaxValueRetriever;
import ca.ulaval.glo4003.service.stock.max.StockMaxValueSinceRange;
import ca.ulaval.glo4003.ws.api.stock.StockDto;
import ca.ulaval.glo4003.ws.api.stock.max.StockMaxResponseDto;
import javax.inject.Inject;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;

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

  public StockDto getStockByName(String name) {
    try {
      Stock stock = stockRepository.getByName(name);
      return stockAssembler.toDto(stock);
    } catch (StockNotFoundException exception) {
      throw new StockDoesNotExistException(exception);
    }
  }

  public StockMaxResponseDto getStockMaxValue(String title, StockMaxValueSinceRange parameter) {
    Stock stock = getStockByTitleOrThrowException(title);
    try {
      HistoricalStockValue maximumValue = stockMaxValueRetriever.getStockMaxValue(stock, parameter);
      return stockMaxResponseAssembler.toDto(title, maximumValue);
    } catch (NoStockValueFitsCriteriaException e) {
      throw new ServerErrorException(Response.Status.INTERNAL_SERVER_ERROR);
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
