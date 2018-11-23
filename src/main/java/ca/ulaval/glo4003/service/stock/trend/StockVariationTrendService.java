package ca.ulaval.glo4003.service.stock.trend;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockHistory;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.service.date.DateService;
import ca.ulaval.glo4003.service.stock.StockDoesNotExistException;
import ca.ulaval.glo4003.service.stock.trend.dto.StockVariationSummary;
import javax.inject.Inject;

@Component
public class StockVariationTrendService {

  private final StockRepository stockRepository;
  private final DateService dateService;
  private final StockVariationCalculator stockVariationCalculator;

  @Inject
  public StockVariationTrendService(StockRepository stockRepository,
                                    DateService dateService,
                                    StockVariationCalculator stockVariationCalculator) {
    this.stockRepository = stockRepository;
    this.dateService = dateService;
    this.stockVariationCalculator = stockVariationCalculator;
  }

  public StockVariationSummary getStockVariationSummary(String stockTitle) {
    try {
      Stock stock = stockRepository.findByTitle(stockTitle);
      StockHistory valueHistory = stock.getValueHistory();

      return new StockVariationSummary(
          stockVariationCalculator.getStockVariationTrendSinceDate(valueHistory, dateService.getFiveDaysAgo()),
          stockVariationCalculator.getStockVariationTrendSinceDate(valueHistory, dateService.getThirtyDaysAgo()),
          stockVariationCalculator.getStockVariationTrendSinceDate(valueHistory, dateService.getOneYearAgo())
      );
    } catch (StockNotFoundException e) {
      throw new StockDoesNotExistException(e);
    }
  }
}
