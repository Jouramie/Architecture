package ca.ulaval.glo4003.service.stock.trend;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValueHistory;
import ca.ulaval.glo4003.infrastructure.injection.Component;
import ca.ulaval.glo4003.service.stock.StockDoesNotExistException;
import ca.ulaval.glo4003.service.stock.trend.dto.StockVariationSummary;
import ca.ulaval.glo4003.service.time.HistoricalDatetimeService;
import javax.inject.Inject;

@Component
public class StockVariationTrendService {

  private final StockRepository stockRepository;
  private final HistoricalDatetimeService historicalDatetimeService;
  private final StockVariationCalculator stockVariationCalculator;

  @Inject
  public StockVariationTrendService(StockRepository stockRepository,
                                    HistoricalDatetimeService historicalDatetimeService,
                                    StockVariationCalculator stockVariationCalculator) {
    this.stockRepository = stockRepository;
    this.historicalDatetimeService = historicalDatetimeService;
    this.stockVariationCalculator = stockVariationCalculator;
  }

  public StockVariationSummary getStockVariationSummary(String stockTitle) {
    try {
      Stock stock = stockRepository.findByTitle(stockTitle);
      StockValueHistory valueHistory = stock.getValueHistory();

      return new StockVariationSummary(
          stockVariationCalculator.getStockVariationTrendSinceDate(valueHistory, historicalDatetimeService.getFiveDaysAgo().toLocalDate()),
          stockVariationCalculator.getStockVariationTrendSinceDate(valueHistory, historicalDatetimeService.getThirtyDaysAgo().toLocalDate()),
          stockVariationCalculator.getStockVariationTrendSinceDate(valueHistory, historicalDatetimeService.getOneYearAgo().toLocalDate())
      );
    } catch (StockNotFoundException e) {
      throw new StockDoesNotExistException(e);
    }
  }
}
