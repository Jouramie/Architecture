package ca.ulaval.glo4003.service.portfolio;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.portfolio.HistoricalPortfolio;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.service.cart.exceptions.InvalidStockTitleException;
import ca.ulaval.glo4003.service.portfolio.dto.HistoricalPortfolioDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioItemDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioReportDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import javax.inject.Inject;

@Component
public class PortfolioReportAssembler {
  private final StockRepository stockRepository;

  @Inject
  public PortfolioReportAssembler(StockRepository stockRepository) {
    this.stockRepository = stockRepository;
  }

  public PortfolioReportDto toDto(TreeSet<HistoricalPortfolio> portfolios, String mostIncreasingStock,
                                  String mostDecreasingStock) throws StockNotFoundException, NoStockValueFitsCriteriaException {
    List<HistoricalPortfolioDto> portfoliosDto = new ArrayList<>();
    for (HistoricalPortfolio portfolio : portfolios) {
      HistoricalPortfolioDto toDto = historicalPortfolioToDto(portfolio);
      portfoliosDto.add(toDto);
    }
    return new PortfolioReportDto(portfoliosDto, mostIncreasingStock, mostDecreasingStock);
  }

  private HistoricalPortfolioDto historicalPortfolioToDto(HistoricalPortfolio historicalPortfolio) throws StockNotFoundException, NoStockValueFitsCriteriaException {
    List<PortfolioItemDto> items = historicalStockCollectionToDto(historicalPortfolio.date, historicalPortfolio.stocks);
    BigDecimal currentTotalValue = historicalPortfolio.getTotal(stockRepository).toUsd();
    return new HistoricalPortfolioDto(historicalPortfolio.date, items, currentTotalValue);
  }

  private List<PortfolioItemDto> historicalStockCollectionToDto(LocalDate date, StockCollection stockCollection) {
    List<PortfolioItemDto> items = new ArrayList<>();
    for (String title : stockCollection.getTitles()) {
      items.add(historicalItemToDto(date, title, stockCollection.getQuantity(title)));
    }
    return items;
  }

  private PortfolioItemDto historicalItemToDto(LocalDate date, String title, int quantity) {
    Stock stock = getStock(title);
    BigDecimal currentValue = stock.getValueOnDate(date).get().getLatestValue().toUsd();
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
