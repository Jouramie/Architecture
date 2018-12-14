package ca.ulaval.glo4003.service.stock.trend;

import ca.ulaval.glo4003.domain.stock.StockTrend;

public class StockVariationSummary {

  public final StockTrend last5days;
  public final StockTrend last30days;
  public final StockTrend lastYear;

  public StockVariationSummary(StockTrend last5days, StockTrend last30days, StockTrend lastYear) {
    this.last5days = last5days;
    this.last30days = last30days;
    this.lastYear = lastYear;
  }
}
