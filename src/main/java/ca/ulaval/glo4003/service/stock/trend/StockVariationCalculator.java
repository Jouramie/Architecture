package ca.ulaval.glo4003.service.stock.trend;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.StockHistory;
import ca.ulaval.glo4003.domain.stock.StockTrend;
import ca.ulaval.glo4003.domain.stock.StockValue;
import java.time.LocalDate;

@Component
public class StockVariationCalculator {

  public StockTrend getStockVariationTrendSinceDate(StockHistory valueHistory, LocalDate date) {
    try {
      HistoricalStockValue latestValue = valueHistory.getLatestValue();
      StockValue valueOnDay = valueHistory.getValueOnDay(date);

      if (valueOnDay.getLatestValue().isGreaterThan(latestValue.value.getLatestValue())) {
        return StockTrend.DECREASING;
      } else if (valueOnDay.getLatestValue().isLessThan(latestValue.value.getLatestValue())) {
        return StockTrend.INCREASING;
      } else {
        return StockTrend.STABLE;
      }
    } catch (NoStockValueFitsCriteriaException e) {
      return StockTrend.NO_DATA;
    }
  }
}
