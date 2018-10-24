package ca.ulaval.glo4003.service.cart;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.service.Component;
import ca.ulaval.glo4003.ws.api.cart.TransactionDto;
import ca.ulaval.glo4003.ws.api.cart.TransactionItemDto;
import java.util.List;
import javax.inject.Inject;

@Component
public class TransactionAssembler {

  private final TransactionItemAssembler itemAssembler;

  @Inject
  public TransactionAssembler(TransactionItemAssembler itemAssembler) {
    this.itemAssembler = itemAssembler;
  }

  public TransactionDto toDto(Transaction transaction) {
    List<TransactionItemDto> itemDtos = transaction.items.stream().map(itemAssembler::toDto).collect(toList());
    return new TransactionDto(transaction.type.toString(), itemDtos, transaction.timestamp);
  }
}
