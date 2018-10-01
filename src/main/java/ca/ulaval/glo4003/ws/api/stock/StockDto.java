package ca.ulaval.glo4003.ws.api.stock;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "StockResponse",
    description = "Stock response containing title, market, stock name, category, "
        + "stock value at market opening, current stock value and stock value at market closeValue."
)
public class StockDto {
  @Schema(description = "Title")
  public final String title;
  @Schema(description = "Market")
  public final String market;
  @Schema(description = "Name of the company")
  public final String name;
  @Schema(description = "Stock value at market opening")
  public final double openValue;
  @Schema(description = "Current stock value")
  public final double currentValue;
  @Schema(description = "Stock value at market closeValue")
  public final double closeValue;
  @Schema(description = "Category")
  public final String category;

  public StockDto(String title, String market, String name, String category,
                  double openValue, double currentValue, double closeValue) {
    this.title = title;
    this.market = market;
    this.name = name;
    this.category = category;
    this.openValue = openValue;
    this.currentValue = currentValue;
    this.closeValue = closeValue;
  }
}
