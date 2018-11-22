package ca.ulaval.glo4003.ws.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(
    name = "Money amount user limit",
    description = "Representation of a money amount per transaction limit."
)
public class ApiMoneyAmountLimitDto extends ApiLimitDto {
  @Schema(
      description = "The maximum money amount the user can spend in a single transaction."
  )
  public final BigDecimal moneyAmount;

  public ApiMoneyAmountLimitDto(LocalDateTime start, LocalDateTime end, BigDecimal moneyAmount) {
    super(start, end);
    this.moneyAmount = moneyAmount;
  }
}
