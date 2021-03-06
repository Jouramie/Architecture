package ca.ulaval.glo4003.service.transaction;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.transaction.TransactionItem;

@Component
public class TransactionItemAssembler {

  public TransactionItemDto toDto(TransactionItem item) {
    return new TransactionItemDto(item.title,
        item.quantity,
        item.amount.getAmount(),
        item.amount.getCurrency().getName());
  }
}
