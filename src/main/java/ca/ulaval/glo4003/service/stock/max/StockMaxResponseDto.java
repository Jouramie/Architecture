package ca.ulaval.glo4003.service.stock.max;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StockMaxResponseDto {

  public final String title;
  public final BigDecimal maximumValue;

  public final LocalDate maximumValueDate;

  public StockMaxResponseDto(String title, BigDecimal maximumValue, LocalDate maximumValueDate) {
    this.title = title;
    this.maximumValue = maximumValue;
    this.maximumValueDate = maximumValueDate;
  }
}
