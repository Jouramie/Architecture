package ca.ulaval.glo4003.service.portfolio;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.query.StockQuery;
import ca.ulaval.glo4003.domain.stock.query.StockQueryBuilder;
import ca.ulaval.glo4003.service.cart.exceptions.InvalidStockTitleException;
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

  public PortfolioDto toDto(Portfolio portfolio) {
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
    BigDecimal currentValue = stock.getValue().getLatestValue().toUsd();
    return new PortfolioItemDto(title, currentValue, quantity);
  }

  private Stock getStock(String title) throws InvalidStockTitleException {
    StockQuery stockQuery = new StockQueryBuilder().withTitle(title).build();
    List<Stock> stocks = stockRepository.find(stockQuery);
    if (stocks.isEmpty()) {
      throw new InvalidStockTitleException(title);
    }
    return stocks.get(0);
  }
}
