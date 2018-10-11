package ca.ulaval.glo4003.ws.api.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionDto {
  @Schema(description = "Type")
  public final String type;
  @Schema(description = "Transaction Items")
  public final List<TransactionItemDto> items;
  @Schema(description = "Transaction Timestamp")
  public final LocalDateTime timestamp;

  public TransactionDto(String type, List<TransactionItemDto> items, LocalDateTime timestamp) {
    this.type = type;
    this.items = items;
    this.timestamp = timestamp;
  }
}
