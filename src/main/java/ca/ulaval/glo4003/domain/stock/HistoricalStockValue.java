package ca.ulaval.glo4003.domain.stock;

import java.time.LocalDate;

public class HistoricalStockValue {
  public final LocalDate date;
  public final StockValue value;

  public HistoricalStockValue(LocalDate date, StockValue value) {
    this.date = date;
    this.value = value;
  }
}
