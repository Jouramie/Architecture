package ca.ulaval.glo4003.util;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import ca.ulaval.glo4003.service.cart.dto.TransactionDto;
import ca.ulaval.glo4003.service.cart.dto.TransactionItemDto;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionDtoBuilder {
  private static final String DEFAULT_TRANSACTION_TYPE = "PURCHASE";
  private static final List<TransactionItemDto> DEFAULT_ITEMS = emptyList();
  private static final LocalDateTime DEFAULT_TIMESTAMP = LocalDateTime.now();

  public TransactionDto build() {
    return new TransactionDto(DEFAULT_TRANSACTION_TYPE, DEFAULT_ITEMS, DEFAULT_TIMESTAMP);
  }

  public List<TransactionDto> buildList() {
    return singletonList(this.build());
  }
}
