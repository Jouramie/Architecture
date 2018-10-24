package ca.ulaval.glo4003.ws.api.cart;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.infrastructure.injection.Component;
import ca.ulaval.glo4003.service.cart.TransactionDto;
import java.util.List;
import javax.inject.Inject;

@Component
public class ApiTransactionAssembler {
  private final ApiTransactionItemAssembler itemAssembler;

  @Inject
  public ApiTransactionAssembler(ApiTransactionItemAssembler itemAssembler) {
    this.itemAssembler = itemAssembler;
  }

  public ApiTransactionDto toDto(TransactionDto transactionDto) {
    List<ApiTransactionItemDto> itemDtos = transactionDto.items.stream().map(itemAssembler::toDto).collect(toList());
    return new ApiTransactionDto(transactionDto.type, itemDtos, transactionDto.timestamp);
  }
}
