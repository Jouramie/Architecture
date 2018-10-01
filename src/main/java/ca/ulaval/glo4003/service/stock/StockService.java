package ca.ulaval.glo4003.service.stock;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.ws.infrastructure.injection.Component;
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
    Stock stock = stockRepository.getByTitle(title);
    return stockAssembler.toDto(stock);
  }

  public StockDto getStockByName(String name) {
    Stock stock = stockRepository.getByName(name);
    return stockAssembler.toDto(stock);
  }
}
