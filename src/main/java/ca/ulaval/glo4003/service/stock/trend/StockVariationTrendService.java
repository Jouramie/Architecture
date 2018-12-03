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

  @Inject
  public StockVariationTrendService(StockRepository stockRepository,
                                    DateService dateService) {
    this.stockRepository = stockRepository;
    this.dateService = dateService;
  }

  public StockVariationSummary getStockVariationSummary(String stockTitle) {
    try {
      Stock stock = stockRepository.findByTitle(stockTitle);
      StockHistory valueHistory = stock.getValueHistory();

      return new StockVariationSummary(
          valueHistory.getStockVariationTrendSinceDate(dateService.getFiveDaysAgo()),
          valueHistory.getStockVariationTrendSinceDate(dateService.getThirtyDaysAgo()),
          valueHistory.getStockVariationTrendSinceDate(dateService.getOneYearAgo())
      );
    } catch (StockNotFoundException e) {
      throw new StockDoesNotExistException(e);
    }
  }
}
