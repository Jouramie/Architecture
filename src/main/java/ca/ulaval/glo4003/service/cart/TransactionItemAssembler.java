package ca.ulaval.glo4003.service.cart;

import ca.ulaval.glo4003.domain.transaction.TransactionItem;
import ca.ulaval.glo4003.infrastructure.injection.Component;

@Component
public class TransactionItemAssembler {

  public TransactionItemDto toDto(TransactionItem item) {
    return new TransactionItemDto(item.title,
        item.quantity,
        item.amount.getAmount(),
        item.amount.getCurrency().getName());
  }
}
