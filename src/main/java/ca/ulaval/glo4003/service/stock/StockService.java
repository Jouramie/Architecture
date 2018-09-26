package ca.ulaval.glo4003.service.stock;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;

class StockService {
  private final StockRepository stockRepository;
  private final StockAssembler stockAssembler;

  StockService(StockRepository stockRepository, StockAssembler stockAssembler) {
    this.stockRepository = stockRepository;
    this.stockAssembler = stockAssembler;
  }

  StockDto getStockByTitle(String title) {
    Stock stock = stockRepository.getByTitle(title);
    return stockAssembler.toDto(stock);
  }

  StockDto getStockByName(String name) {
    Stock stock = stockRepository.getByName(name);
    return stockAssembler.toDto(stock);
  }
}
