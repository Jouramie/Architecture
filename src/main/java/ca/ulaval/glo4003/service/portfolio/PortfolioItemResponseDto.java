package ca.ulaval.glo4003.service.portfolio;

import java.math.BigDecimal;

public class PortfolioItemResponseDto {
  public final String title;
  public final BigDecimal currentValue;
  public final int quantity;

  public PortfolioItemResponseDto(String title, BigDecimal currentValue, int quantity) {
    this.title = title;
    this.currentValue = currentValue;
    this.quantity = quantity;
  }
}
