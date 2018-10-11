package ca.ulaval.glo4003.ws.api.stock.trend;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "StockTrendResponce",
    description = "Stock trends for a given stock title."
)
public class StockTrendDto {
  @Schema(description = "Relevant stock title")
  public final String stockTitle;
  @Schema(description = "Variation trend for the last 5 days")
  public final StockTrend lastWorkWeek;
  @Schema(description = "Variation trend for the last 30 days")
  public final StockTrend lastMonth;
  @Schema(description = "Variation trend for the last year")
  public final StockTrend lastYear;

  public StockTrendDto(String stockTitle, StockTrend lastWorkWeek, StockTrend lastMonth, StockTrend lastYear) {
    this.stockTitle = stockTitle;
    this.lastWorkWeek = lastWorkWeek;
    this.lastMonth = lastMonth;
    this.lastYear = lastYear;
  }
}
