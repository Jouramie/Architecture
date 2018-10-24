package ca.ulaval.glo4003.ws.api.stock.max;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "Stock maximum"
)
public class StockMaxResponseDto {
  @Schema(description = "Stock title")
  public final String title;
  @Schema(description = "Maximum value during the last five days")
  public final StockMaxResponseValueDto lastFiveDays;
  @Schema(description = "Maximum value during the current month (from first day of the current"
      + " month to today")
  public final StockMaxResponseValueDto currentMonth;
  @Schema(description = "Maximum value during the last 30 days")
  public final StockMaxResponseValueDto lastMonth;
  @Schema(description = "Maximum value during the last year")
  public final StockMaxResponseValueDto lastYear;
  @Schema(description = "Maximum value during the last five years")
  public final StockMaxResponseValueDto lastFiveYears;
  @Schema(description = "Maximum value during the last ten years")
  public final StockMaxResponseValueDto lastTenYears;
  @Schema(description = "Maximum value of all the data available")
  public final StockMaxResponseValueDto allTime;

  public StockMaxResponseDto(String title,
                             StockMaxResponseValueDto lastFiveDays,
                             StockMaxResponseValueDto currentMonth,
                             StockMaxResponseValueDto lastMonth,
                             StockMaxResponseValueDto lastYear,
                             StockMaxResponseValueDto lastFiveYears,
                             StockMaxResponseValueDto lastTenYears,
                             StockMaxResponseValueDto allTime) {
    this.title = title;
    this.lastFiveDays = lastFiveDays;
    this.currentMonth = currentMonth;
    this.lastMonth = lastMonth;
    this.lastYear = lastYear;
    this.lastFiveYears = lastFiveYears;
    this.lastTenYears = lastTenYears;
    this.allTime = allTime;
  }
}
