package ca.ulaval.glo4003.service.cart;

import java.time.LocalDateTime;
import java.util.List;

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
