package ca.ulaval.glo4003.ws.api.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(
    name = "Transaction"
)
public class TransactionDto {
  public final String type;
  public final List<TransactionItemDto> items;
  public final LocalDateTime timestamp;

  public TransactionDto(String type, List<TransactionItemDto> items, LocalDateTime timestamp) {
    this.type = type;
    this.items = items;
    this.timestamp = timestamp;
  }
}
