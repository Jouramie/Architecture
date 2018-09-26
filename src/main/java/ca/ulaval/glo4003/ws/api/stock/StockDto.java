package ca.ulaval.glo4003.ws.api.stock;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "StockResponse",
    description = "Stock response containing title, market, stock name, category, "
        + "stock value at market opening, current stock value and stock value at market close."
)
public class StockDto {
  @Schema(description = "Title")
  public final String title;
  @Schema(description = "Market")
  public final String market;
  @Schema(description = "Name of the company")
  public final String name;
  @Schema(description = "Stock value at market opening")
  public final double open;
  @Schema(description = "Current stock value")
  public final double current;
  @Schema(description = "Stock value at market close")
  public final double close;
  @Schema(description = "Category")
  private final String category;

  public StockDto(String title, String market, String name, String category,
                  double open, double current, double close) {
    this.title = title;
    this.market = market;
    this.name = name;
    this.category = category;
    this.open = open;
    this.current = current;
    this.close = close;
  }
}
