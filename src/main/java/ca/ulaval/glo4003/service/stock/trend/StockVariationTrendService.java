package ca.ulaval.glo4003.service.stock.trend;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockHistory;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.query.StockQuery;
import ca.ulaval.glo4003.domain.stock.query.StockQueryBuilder;
import ca.ulaval.glo4003.service.date.DateService;
import ca.ulaval.glo4003.service.stock.StockDoesNotExistException;
import ca.ulaval.glo4003.service.stock.trend.dto.StockVariationSummary;
import java.util.List;
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
    StockQuery stockQuery = new StockQueryBuilder().withTitle(stockTitle).build();
    List<Stock> stocks = stockRepository.find(stockQuery);
    if (stocks.isEmpty()) {
      throw new StockDoesNotExistException(stockTitle);
    }
    StockHistory valueHistory = stocks.get(0).getValueHistory();

    return new StockVariationSummary(
        stockVariationCalculator.getStockVariationTrendSinceDate(valueHistory, dateService.getFiveDaysAgo()),
        stockVariationCalculator.getStockVariationTrendSinceDate(valueHistory, dateService.getThirtyDaysAgo()),
        stockVariationCalculator.getStockVariationTrendSinceDate(valueHistory, dateService.getOneYearAgo())
    );
  }
}
