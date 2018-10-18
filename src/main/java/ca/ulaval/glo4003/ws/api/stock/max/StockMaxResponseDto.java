package ca.ulaval.glo4003.ws.api.stock.max;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(
    name = "StockMaxResponse",
    description = "Stock maximum response containing title, the maximum value and the date the value was reached."
)
public class StockMaxResponseDto {
  @Schema(description = "Title")
  public final String title;
  @Schema(description = "Maximum value")
  public final BigDecimal maximumValue;
  @Schema(description = "Date at which the maximum value was reached")
  @JsonFormat
  public final LocalDate maximumValueDate;

  @JsonCreator
  public StockMaxResponseDto(@JsonProperty("title") String title,
                             @JsonProperty("maximumValue") BigDecimal maximumValue,
                             @JsonProperty("date") LocalDate maximumValueDate) {
    this.title = title;
    this.maximumValue = maximumValue;
    this.maximumValueDate = maximumValueDate;
  }
}
