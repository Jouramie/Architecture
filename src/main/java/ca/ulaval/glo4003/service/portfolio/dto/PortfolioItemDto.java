package ca.ulaval.glo4003.service.portfolio.dto;

import java.math.BigDecimal;

public class PortfolioItemDto {
  public final String title;
  public final BigDecimal currentValue;
  public final int quantity;

  public PortfolioItemDto(String title, BigDecimal currentValue, int quantity) {
    this.title = title;
    this.currentValue = currentValue;
    this.quantity = quantity;
  }
}
