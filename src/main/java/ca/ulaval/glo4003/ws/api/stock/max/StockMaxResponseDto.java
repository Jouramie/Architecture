package ca.ulaval.glo4003.ws.api.stock.max;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(
    name = "Stock maximum"
)
public class StockMaxResponseDto {
  public final String title;
  public final BigDecimal maximumValue;
  @Schema(description = "Date on which the maximum value was reached.")
  @JsonFormat
  public final LocalDate maximumValueDate;

  public StockMaxResponseDto(String title, BigDecimal maximumValue, LocalDate maximumValueDate) {
    this.title = title;
    this.maximumValue = maximumValue;
    this.maximumValueDate = maximumValueDate;
  }
}
