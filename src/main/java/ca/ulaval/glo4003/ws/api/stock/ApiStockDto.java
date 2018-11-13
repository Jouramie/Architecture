package ca.ulaval.glo4003.ws.api.stock;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(
    name = "Stock"
)
public class ApiStockDto {
  public final String title;
  public final String market;
  @Schema(description = "Name of the company")
  public final String name;
  public final String category;
  @Schema(description = "Stock value at market opening")
  public final BigDecimal openValue;
  @Schema(description = "Current stock value")
  public final BigDecimal currentValue;
  @Schema(description = "Stock value at market saveClosingPrice")
  public final BigDecimal closeValue;

  public ApiStockDto(String title,
                     String name,
                     String category,
                     String market,
                     BigDecimal openValue,
                     BigDecimal currentValue,
                     BigDecimal closeValue) {
    this.title = title;
    this.market = market;
    this.name = name;
    this.category = category;
    this.openValue = openValue;
    this.currentValue = currentValue;
    this.closeValue = closeValue;
  }
}
