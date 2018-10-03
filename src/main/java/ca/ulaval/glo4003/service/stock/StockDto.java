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
  public final BigDecimal openValue;
  @Schema(description = "Current stock value")
  public final BigDecimal currentValue;
  @Schema(description = "Stock value at market close")
  public final BigDecimal closeValue;
  @Schema(description = "Category")
  public final String category;

  @JsonCreator
  public StockDto(@JsonProperty("title") String title,
                  @JsonProperty("name") String name,
                  @JsonProperty("category") String category,
                  @JsonProperty("market") String market,
                  @JsonProperty("openValue") BigDecimal openValue,
                  @JsonProperty("currentValue") BigDecimal currentValue,
                  @JsonProperty("closeValue") BigDecimal closeValue) {
    this.title = title;
    this.market = market;
    this.name = name;
    this.category = category;
    this.openValue = openValue;
    this.currentValue = currentValue;
    this.closeValue = closeValue;
  }
}
