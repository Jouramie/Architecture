package ca.ulaval.glo4003.service.portfolio;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.portfolio.InvalidStockInPortfolioException;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.exception.StockNotFoundException;
import ca.ulaval.glo4003.service.cart.exception.InvalidStockTitleException;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioItemDto;
import java.math.BigDecimal;
import java.util.List;
import javax.inject.Inject;

@Component
public class PortfolioAssembler {
  private final StockRepository stockRepository;

  @Inject
  public PortfolioAssembler(StockRepository stockRepository) {
    this.stockRepository = stockRepository;
  }

  public PortfolioDto toDto(Portfolio portfolio) throws InvalidStockInPortfolioException {
    List<PortfolioItemDto> items = stockCollectionToDto(portfolio.getStocks());
    BigDecimal currentTotalValue = portfolio.getCurrentTotalValue(stockRepository).toUsd();
    return new PortfolioDto(items, currentTotalValue);
  }

  private List<PortfolioItemDto> stockCollectionToDto(StockCollection stockCollection) {
    return stockCollection.getTitles().stream()
        .map((title) -> itemToDto(title, stockCollection.getQuantity(title)))
        .collect(toList());
  }

  private PortfolioItemDto itemToDto(String title, int quantity) {
    Stock stock = getStock(title);
    BigDecimal currentValue = stock.getCurrentValue().toUsd();
    return new PortfolioItemDto(title, currentValue, quantity);
  }

  private Stock getStock(String title) {
    try {
      return stockRepository.findByTitle(title);
    } catch (StockNotFoundException exception) {
      throw new InvalidStockTitleException(exception);
    }
  }
}
