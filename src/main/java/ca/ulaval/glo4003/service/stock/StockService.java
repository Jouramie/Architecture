package ca.ulaval.glo4003.service.stock;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.infrastructure.injection.Component;
import ca.ulaval.glo4003.ws.api.stock.StockDto;
import javax.inject.Inject;

@Component
public class StockService {
  private final StockRepository stockRepository;
  private final StockAssembler stockAssembler;

  @Inject
  public StockService(StockRepository stockRepository, StockAssembler stockAssembler) {
    this.stockRepository = stockRepository;
    this.stockAssembler = stockAssembler;
  }

  public StockDto getStockByTitle(String title) {
    try {
      Stock stock = stockRepository.getByTitle(title);
      return stockAssembler.toDto(stock);
    } catch (StockNotFoundException exception) {
      throw new StockDoesNotExistException(exception);
    }
  }

  public StockDto getStockByName(String name) {
    try {
      Stock stock = stockRepository.getByName(name);
      return stockAssembler.toDto(stock);
    } catch (StockNotFoundException exception) {
      throw new StockDoesNotExistException(exception);
    }
  }
}
