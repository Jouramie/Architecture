package ca.ulaval.glo4003.service.stock.max;

import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;

public class StockMaxValueSummary {
  public final HistoricalStockValue lastFiveDays;
  public final HistoricalStockValue currentMonth;
  public final HistoricalStockValue lastMonth;
  public final HistoricalStockValue lastYear;
  public final HistoricalStockValue lastFiveYears;
  public final HistoricalStockValue lastTenYears;
  public final HistoricalStockValue allTime;

  public StockMaxValueSummary(HistoricalStockValue lastFiveDays,
                              HistoricalStockValue currentMonth,
                              HistoricalStockValue lastMonth,
                              HistoricalStockValue lastYear,
                              HistoricalStockValue lastFiveYears,
                              HistoricalStockValue lastTenYears,
                              HistoricalStockValue allTime) {
    this.lastFiveDays = lastFiveDays;
    this.currentMonth = currentMonth;
    this.lastMonth = lastMonth;
    this.lastYear = lastYear;
    this.lastFiveYears = lastFiveYears;
    this.lastTenYears = lastTenYears;
    this.allTime = allTime;
  }
}
