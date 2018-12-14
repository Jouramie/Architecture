package ca.ulaval.glo4003.ws.api.stock.dto;

import ca.ulaval.glo4003.domain.stock.StockTrend;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "Stock trend"
)
public class ApiStockTrendDto {
  public final String title;
  @Schema(description = "Variation trend for the last 5 days")
  public final StockTrend last5Days;
  @Schema(description = "Variation trend for the last 30 days")
  public final StockTrend last30Days;
  @Schema(description = "Variation trend for the last year")
  public final StockTrend lastYear;

  public ApiStockTrendDto(String title,
                          StockTrend last5Days,
                          StockTrend last30Days,
                          StockTrend lastYear) {
    this.title = title;
    this.last5Days = last5Days;
    this.last30Days = last30Days;
    this.lastYear = lastYear;
  }
}
