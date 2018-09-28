package ca.ulaval.glo4003.service.stock;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

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
  public final BigDecimal open;
  @Schema(description = "Current stock value")
  public final BigDecimal current;
  @Schema(description = "Category")
  public final String category;
  @Schema(description = "Stock value at market close")
  public final BigDecimal close;

  @JsonCreator
  public StockDto(@JsonProperty("title") String title,
                  @JsonProperty("name") String name,
                  @JsonProperty("category") String category,
                  @JsonProperty("market") String market,
                  @JsonProperty("open") BigDecimal open,
                  @JsonProperty("current") BigDecimal current,
                  @JsonProperty("close") BigDecimal close) {
    this.title = title;
    this.market = market;
    this.name = name;
    this.category = category;
    this.open = open;
    this.current = current;
    this.close = close;
  }
}
