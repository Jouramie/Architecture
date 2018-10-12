package ca.ulaval.glo4003.domain.stock;

import java.time.LocalDate;

public class StockValueHistorianResult {
  public final LocalDate date;
  public final StockValue value;

  public StockValueHistorianResult(LocalDate date, StockValue value) {
    this.date = date;
    this.value = value;
  }
}
