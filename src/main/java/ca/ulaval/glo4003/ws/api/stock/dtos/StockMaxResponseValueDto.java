package ca.ulaval.glo4003.ws.api.stock.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

public class StockMaxResponseValueDto {
  @Schema(description = "Maximum value of the stock (in USD)")
  public final BigDecimal maximumValue;
  @Schema(description = "Date on which the maximum value was reached.")
  @JsonFormat
  public final LocalDate maximumValueDate;

  public StockMaxResponseValueDto(BigDecimal maximumValue, LocalDate maximumValueDate) {
    this.maximumValue = maximumValue;
    this.maximumValueDate = maximumValueDate;
  }
}
