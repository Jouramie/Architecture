package ca.ulaval.glo4003.service.stock;

import java.math.BigDecimal;

public class StockDto {
  public final String title;
  public final String market;

  public final String name;
  public final String category;

  public final BigDecimal openValue;

  public final BigDecimal currentValue;

  public final BigDecimal closeValue;

  public StockDto(String title,
                  String name,
                  String category,
                  String market,
                  BigDecimal openValue,
                  BigDecimal currentValue,
                  BigDecimal closeValue) {
    this.title = title;
    this.market = market;
    this.name = name;
    this.category = category;
    this.openValue = openValue;
    this.currentValue = currentValue;
    this.closeValue = closeValue;
  }
}
