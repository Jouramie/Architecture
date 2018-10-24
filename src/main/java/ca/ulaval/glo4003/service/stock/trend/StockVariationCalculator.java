package ca.ulaval.glo4003.service.stock.trend;

import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.StockTrend;
import ca.ulaval.glo4003.domain.stock.StockValue;
import ca.ulaval.glo4003.domain.stock.StockValueHistory;
import ca.ulaval.glo4003.service.Component;
import java.time.LocalDate;

@Component
public class StockVariationCalculator {

  public StockTrend getStockVariationTrendSinceDate(StockValueHistory valueHistory, LocalDate date) {
    try {
      HistoricalStockValue latestValue = valueHistory.getLatestValue();
      StockValue valueOnDay = valueHistory.getValueOnDay(date);

      if (valueOnDay.getCurrentValue().isGreaterThan(latestValue.value.getCurrentValue())) {
        return StockTrend.DECREASING;
      } else if (valueOnDay.getCurrentValue().isLessThan(latestValue.value.getCurrentValue())) {
        return StockTrend.INCREASING;
      } else {
        return StockTrend.STABLE;
      }
    } catch (NoStockValueFitsCriteriaException e) {
      return StockTrend.NO_DATA;
    }
  }
}
