package ca.ulaval.glo4003.service.stock.max;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.service.Component;
import ca.ulaval.glo4003.service.InternalErrorException;
import ca.ulaval.glo4003.service.stock.StockDoesNotExistException;
import ca.ulaval.glo4003.service.stock.max.dto.StockMaxValueSummary;
import ca.ulaval.glo4003.service.time.HistoricalDateService;
import java.time.LocalDate;
import javax.inject.Inject;

@Component
public class StockMaxValueService {
  private final StockRepository stockRepository;
  private final HistoricalDateService historicalDateService;
  private final Clock clock;

  @Inject
  public StockMaxValueService(StockRepository stockRepository,
                              HistoricalDateService historicalDateService,
                              Clock clock) {
    this.stockRepository = stockRepository;
    this.historicalDateService = historicalDateService;
    this.clock = clock;
  }

  public StockMaxValueSummary getStockMaxValue(String title) {
    Stock stock = getStockByTitleOrThrowException(title);

    HistoricalStockValue lastFiveDays = getStockMaxValueFrom(stock, historicalDateService.getFiveDaysAgo());
    HistoricalStockValue currentMonth = getStockMaxValueFrom(stock, historicalDateService.getStartOfCurrentMonth());
    HistoricalStockValue lastMonth = getStockMaxValueFrom(stock, historicalDateService.getThirtyDaysAgo());
    HistoricalStockValue lastYear = getStockMaxValueFrom(stock, historicalDateService.getOneYearAgo());
    HistoricalStockValue lastFiveYears = getStockMaxValueFrom(stock, historicalDateService.getFiveYearsAgo());
    HistoricalStockValue lastTenYears = getStockMaxValueFrom(stock, historicalDateService.getTenYearsAgo());
    HistoricalStockValue allTime = getStockMaxValueFrom(stock, LocalDate.MIN);
    return new StockMaxValueSummary(lastFiveDays, currentMonth, lastMonth, lastYear, lastFiveYears, lastTenYears, allTime);
  }

  private Stock getStockByTitleOrThrowException(String title) {
    try {
      return stockRepository.findByTitle(title);
    } catch (StockNotFoundException exception) {
      throw new StockDoesNotExistException(exception);
    }
  }

  private HistoricalStockValue getStockMaxValueFrom(Stock stock, LocalDate from) {
    try {
      return stock.getValueHistory().getMaxValue(from, clock.getCurrentTime().toLocalDate());
    } catch (NoStockValueFitsCriteriaException e) {
      throw new InternalErrorException("No stock value fits criteria.");
    }
  }
}
