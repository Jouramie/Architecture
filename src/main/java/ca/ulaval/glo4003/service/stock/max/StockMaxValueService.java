package ca.ulaval.glo4003.service.stock.max;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.query.StockQuery;
import ca.ulaval.glo4003.domain.stock.query.StockQueryBuilder;
import ca.ulaval.glo4003.service.InternalErrorException;
import ca.ulaval.glo4003.service.date.DateService;
import ca.ulaval.glo4003.service.stock.StockDoesNotExistException;
import ca.ulaval.glo4003.service.stock.max.dto.StockMaxValueSummary;
import java.time.LocalDate;
import java.util.List;
import javax.inject.Inject;

@Component
public class StockMaxValueService {
  private final StockRepository stockRepository;
  private final DateService dateService;
  private final Clock clock;

  @Inject
  public StockMaxValueService(StockRepository stockRepository,
                              DateService dateService,
                              Clock clock) {
    this.stockRepository = stockRepository;
    this.dateService = dateService;
    this.clock = clock;
  }

  public StockMaxValueSummary getStockMaxValue(String title) {
    Stock stock = getStockByTitleOrThrowException(title);

    HistoricalStockValue lastFiveDays = getStockMaxValueFrom(stock, dateService.getFiveDaysAgo());
    HistoricalStockValue currentMonth = getStockMaxValueFrom(stock, dateService.getStartOfCurrentMonth());
    HistoricalStockValue lastMonth = getStockMaxValueFrom(stock, dateService.getThirtyDaysAgo());
    HistoricalStockValue lastYear = getStockMaxValueFrom(stock, dateService.getOneYearAgo());
    HistoricalStockValue lastFiveYears = getStockMaxValueFrom(stock, dateService.getFiveYearsAgo());
    HistoricalStockValue lastTenYears = getStockMaxValueFrom(stock, dateService.getTenYearsAgo());
    HistoricalStockValue allTime = getStockMaxValueFrom(stock, LocalDate.MIN);
    return new StockMaxValueSummary(lastFiveDays, currentMonth, lastMonth, lastYear, lastFiveYears, lastTenYears, allTime);
  }

  private Stock getStockByTitleOrThrowException(String title) {
    StockQuery stockQuery = new StockQueryBuilder().withTitle(title).build();
    List<Stock> stocks = stockRepository.find(stockQuery);
    if (stocks.isEmpty()) {
      throw new StockDoesNotExistException(title);
    }
    return stocks.get(0);
  }

  private HistoricalStockValue getStockMaxValueFrom(Stock stock, LocalDate from) {
    try {
      return stock.getValueHistory().getMaxValue(from, clock.getCurrentTime().toLocalDate());
    } catch (NoStockValueFitsCriteriaException e) {
      throw new InternalErrorException("No stock value fits criteria.");
    }
  }
}
