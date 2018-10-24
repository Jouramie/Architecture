package ca.ulaval.glo4003.service.stock.trend;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValueHistory;
import ca.ulaval.glo4003.service.Component;
import ca.ulaval.glo4003.service.stock.StockDoesNotExistException;
import ca.ulaval.glo4003.service.stock.trend.dto.StockVariationSummary;
import ca.ulaval.glo4003.service.time.HistoricalDateService;
import javax.inject.Inject;

@Component
public class StockVariationTrendService {

  private final StockRepository stockRepository;
  private final HistoricalDateService historicalDateService;
  private final StockVariationCalculator stockVariationCalculator;

  @Inject
  public StockVariationTrendService(StockRepository stockRepository,
                                    HistoricalDateService historicalDateService,
                                    StockVariationCalculator stockVariationCalculator) {
    this.stockRepository = stockRepository;
    this.historicalDateService = historicalDateService;
    this.stockVariationCalculator = stockVariationCalculator;
  }

  public StockVariationSummary getStockVariationSummary(String stockTitle) {
    try {
      Stock stock = stockRepository.findByTitle(stockTitle);
      StockValueHistory valueHistory = stock.getValueHistory();

      return new StockVariationSummary(
          stockVariationCalculator.getStockVariationTrendSinceDate(valueHistory, historicalDateService.getFiveDaysAgo()),
          stockVariationCalculator.getStockVariationTrendSinceDate(valueHistory, historicalDateService.getThirtyDaysAgo()),
          stockVariationCalculator.getStockVariationTrendSinceDate(valueHistory, historicalDateService.getOneYearAgo())
      );
    } catch (StockNotFoundException e) {
      throw new StockDoesNotExistException(e);
    }
  }
}
