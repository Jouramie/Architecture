package ca.ulaval.glo4003.ws.api.cart;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "CartStockResponse",
    description = "Cart stock response containing title, market, stock name, category, "
        + "current stock value and quantity of that stock."
)
public class CartStockResponse {
  @Schema(description = "Title")
  public final String title;
  @Schema(description = "Market")
  public final String market;
  @Schema(description = "Name of the company")
  public final String name;
  @Schema(description = "Category")
  public final String category;
  @Schema(description = "Current stock value")
  public final double currentValue;
  @Schema(description = "Quantity")
  public final int quantity;


  public CartStockResponse(String title, String market, String name, String category,
                           double currentValue, int quantity) {
    this.title = title;
    this.market = market;
    this.name = name;
    this.category = category;
    this.currentValue = currentValue;
    this.quantity = quantity;
  }
}
