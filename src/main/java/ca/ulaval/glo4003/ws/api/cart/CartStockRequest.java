package ca.ulaval.glo4003.ws.api.cart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "CartStockRequest",
    description = "Cart stock request containing the title and the quantity of that stock."
)
public class CartStockRequest {
  @Schema(description = "Title")
  public final String title;
  @Schema(description = "Quantity")
  public final Integer quantity;

  @JsonCreator
  public CartStockRequest(@JsonProperty("title") String title,
                          @JsonProperty("quantity") Integer quantity) {
    this.title = title;
    this.quantity = quantity;
  }
}
