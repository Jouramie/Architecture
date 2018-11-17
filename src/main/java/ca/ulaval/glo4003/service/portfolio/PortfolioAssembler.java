package ca.ulaval.glo4003.service.portfolio;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.portfolio.HistoricPortfolio;
import ca.ulaval.glo4003.domain.portfolio.InvalidStockInPortfolioException;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.service.cart.exceptions.InvalidStockTitleException;
import ca.ulaval.glo4003.service.portfolio.dto.HistoricalPortfolioDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioHistoryDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioItemDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
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

  public PortfolioHistoryDto toDto(TreeSet<HistoricPortfolio> portfolios) throws StockNotFoundException, NoStockValueFitsCriteriaException {
    List<HistoricalPortfolioDto> portfoliosDto = new ArrayList<>();
    for (HistoricPortfolio portfolio : portfolios) {
      HistoricalPortfolioDto toDto = historicalPortfolioToDto(portfolio);
      portfoliosDto.add(toDto);
    }
    return new PortfolioHistoryDto(portfoliosDto);
  }

  private List<PortfolioItemDto> stockCollectionToDto(StockCollection stockCollection) {
    return stockCollection.getTitles().stream()
        .map((title) -> itemToDto(title, stockCollection.getQuantity(title)))
        .collect(toList());
  }

  private PortfolioItemDto itemToDto(String title, int quantity) {
    Stock stock = getStock(title);
    BigDecimal currentValue = stock.getValue().getCurrentValue().toUsd();
    return new PortfolioItemDto(title, currentValue, quantity);
  }

  private HistoricalPortfolioDto historicalPortfolioToDto(HistoricPortfolio historicPortfolio) throws StockNotFoundException, NoStockValueFitsCriteriaException {
    List<PortfolioItemDto> items = historicalStockCollectionToDto(historicPortfolio.date, historicPortfolio.stocks);
    BigDecimal currentTotalValue = historicPortfolio.getTotal(stockRepository).toUsd();
    return new HistoricalPortfolioDto(historicPortfolio.date, items, currentTotalValue);
  }

  private List<PortfolioItemDto> historicalStockCollectionToDto(LocalDate date, StockCollection stockCollection) throws NoStockValueFitsCriteriaException {
    List<PortfolioItemDto> items = new ArrayList<>();
    for (String title : stockCollection.getTitles()) {
      items.add(historicalItemToDto(date, title, stockCollection.getQuantity(title)));
    }
    return items;
  }

  private PortfolioItemDto historicalItemToDto(LocalDate date, String title, int quantity) throws NoStockValueFitsCriteriaException {
    Stock stock = getStock(title);
    BigDecimal currentValue = stock.getValueHistory().getValueOnDay(date).getCurrentValue().toUsd();
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
