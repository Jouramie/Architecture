package ca.ulaval.glo4003.service.stock.max;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.service.InternalErrorException;
import ca.ulaval.glo4003.service.date.DateService;
import ca.ulaval.glo4003.service.stock.StockDoesNotExistException;
import ca.ulaval.glo4003.service.stock.max.dto.StockMaxValueSummary;
import java.time.LocalDate;
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
    try {
      return stockRepository.findByTitle(title);
    } catch (StockNotFoundException exception) {
      throw new StockDoesNotExistException(exception);
    }
  }

  private HistoricalStockValue getStockMaxValueFrom(Stock stock, LocalDate from) {
    return stock.getValueHistory().getMaxValue(from, clock.getCurrentTime().toLocalDate())
        .orElseThrow(() -> new InternalErrorException("No stock value fits criteria."));
  }
}
