package ca.ulaval.glo4003.service.cart.assemblers;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.service.cart.dto.TransactionDto;
import ca.ulaval.glo4003.service.cart.dto.TransactionItemDto;
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

  public List<TransactionDto> toDtoList(List<Transaction> transactions) {
    return transactions.stream().map(this::toDto).collect(toList());
  }
}
