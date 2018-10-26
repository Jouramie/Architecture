package ca.ulaval.glo4003.service.portfolio;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.portfolio.InvalidStockInPortfolioException;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.service.Component;
import ca.ulaval.glo4003.service.cart.InvalidStockTitleException;
import ca.ulaval.glo4003.ws.api.portfolio.PortfolioItemResponseDto;
import ca.ulaval.glo4003.ws.api.portfolio.PortfolioResponseDto;
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

  public PortfolioResponseDto toDto(Portfolio portfolio) throws InvalidStockInPortfolioException {
    List<PortfolioItemResponseDto> items = portfolio.getStocks().getTitles().stream()
        .map((title) -> itemToDto(title, portfolio.getQuantity(title))).collect(toList());
    BigDecimal currentTotalValue = portfolio.getCurrentTotalValue(stockRepository).getAmount();
    return new PortfolioResponseDto(items, currentTotalValue);
  }

  private PortfolioItemResponseDto itemToDto(String title, int quantity) {
    Stock stock = getStock(title);
    BigDecimal currentValue = stock.getValue().getCurrentValue().toUsd();
    return new PortfolioItemResponseDto(title, currentValue, quantity);
  }

  private Stock getStock(String title) {
    try {
      return stockRepository.findByTitle(title);
    } catch (StockNotFoundException exception) {
      throw new InvalidStockTitleException(exception);
    }
  }
}
