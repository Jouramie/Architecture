package ca.ulaval.glo4003.ws.api.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(
    name = "Cart item",
    description = "Representation of a stock with the quantity of that stock in the cart."
)
public class ApiCartItemResponseDto {
  public final String title;
  public final String market;
  @Schema(description = "Name of the company")
  public final String name;
  public final String category;
  public final BigDecimal currentValue;
  public final int quantity;

  public ApiCartItemResponseDto(String title, String market, String name, String category,
                                BigDecimal currentValue, int quantity) {
    this.title = title;
    this.market = market;
    this.name = name;
    this.category = category;
    this.currentValue = currentValue;
    this.quantity = quantity;
  }
}
