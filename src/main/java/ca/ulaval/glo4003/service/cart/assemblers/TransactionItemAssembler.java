package ca.ulaval.glo4003.service.cart.assemblers;

import ca.ulaval.glo4003.domain.transaction.TransactionItem;
import ca.ulaval.glo4003.service.Component;
import ca.ulaval.glo4003.service.cart.dto.TransactionItemDto;

@Component
public class TransactionItemAssembler {

  public TransactionItemDto toDto(TransactionItem item) {
    return new TransactionItemDto(item.title,
        item.quantity,
        item.amount.getAmount(),
        item.amount.getCurrency().getName());
  }
}
