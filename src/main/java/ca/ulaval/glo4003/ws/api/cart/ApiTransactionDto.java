package ca.ulaval.glo4003.ws.api.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(
    name = "Transaction"
)
public class ApiTransactionDto {
  public final String type;
  public final List<ApiTransactionItemDto> items;
  public final LocalDateTime timestamp;

  public ApiTransactionDto(String type, List<ApiTransactionItemDto> items, LocalDateTime timestamp) {
    this.type = type;
    this.items = items;
    this.timestamp = timestamp;
  }
}
