package ca.ulaval.glo4003.ws.api.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public class ApiTransactionItemDto {

  @Schema(description = "Title")
  public final String title;
  @Schema(description = "Quantity")
  public final int quantity;
  @Schema(description = "Money Amount")
  public final BigDecimal moneyAmount;
  @Schema(description = "Currency")
  public final String currency;

  public ApiTransactionItemDto(String title, int quantity, BigDecimal moneyAmount, String currency) {
    this.title = title;
    this.quantity = quantity;
    this.moneyAmount = moneyAmount;
    this.currency = currency;
  }
}
