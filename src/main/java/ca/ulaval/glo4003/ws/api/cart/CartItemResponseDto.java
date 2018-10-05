package ca.ulaval.glo4003.ws.api.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(
    name = "CartItemResponseDto",
    description = "Cart stock response containing title, market, stock name, category, "
        + "current stock value and quantity of that stock."
)
public class CartItemResponseDto {
  @Schema(description = "Title")
  public final String title;
  @Schema(description = "Market")
  public final String market;
  @Schema(description = "Name of the company")
  public final String name;
  @Schema(description = "Category")
  public final String category;
  @Schema(description = "Current stock value")
  public final BigDecimal currentValue;
  @Schema(description = "Quantity")
  public final int quantity;

  public CartItemResponseDto(String title, String market, String name, String category,
                             BigDecimal currentValue, int quantity) {
    this.title = title;
    this.market = market;
    this.name = name;
    this.category = category;
    this.currentValue = currentValue;
    this.quantity = quantity;
  }
}
