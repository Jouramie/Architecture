package ca.ulaval.glo4003.ws.api.portfolio;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "PortfolioItemResponseDto",
    description = "Portfolio stock response containing title and quantity of that stock."
)
public class PortfolioItemResponseDto {
  @Schema(description = "Title")
  public final String title;
  @Schema(description = "Quantity")
  public final int quantity;

  public PortfolioItemResponseDto(String title, int quantity) {
    this.title = title;
    this.quantity = quantity;
  }
}
