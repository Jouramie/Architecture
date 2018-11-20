package ca.ulaval.glo4003.ws.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(
    name = "Money amount user limit",
    description = "Representation of a maximal money amount per transaction limit."
)
public class ApiMoneyAmountLimitDto extends ApiLimitDto {
  @Schema(
      description = "The maximal money amount the user can spend in a single transaction."
  )
  public final BigDecimal maximalMoneySpent;

  public ApiMoneyAmountLimitDto(LocalDateTime start, LocalDateTime end, BigDecimal maximalMoneySpent) {
    super(start, end);
    this.maximalMoneySpent = maximalMoneySpent;
  }
}
