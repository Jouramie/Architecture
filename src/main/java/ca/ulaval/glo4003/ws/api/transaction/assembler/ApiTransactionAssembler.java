package ca.ulaval.glo4003.ws.api.transaction.assembler;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.service.cart.dto.TransactionDto;
import ca.ulaval.glo4003.ws.api.transaction.dto.ApiTransactionDto;
import ca.ulaval.glo4003.ws.api.transaction.dto.ApiTransactionItemDto;
import java.util.List;
import javax.inject.Inject;

@Component
public class ApiTransactionAssembler {
  private final ApiTransactionItemAssembler itemAssembler;

  @Inject
  public ApiTransactionAssembler(ApiTransactionItemAssembler itemAssembler) {
    this.itemAssembler = itemAssembler;
  }

  public List<ApiTransactionDto> toDtoList(List<TransactionDto> transactionDtos) {
    return transactionDtos.stream().map(this::toDto).collect(toList());
  }

  public ApiTransactionDto toDto(TransactionDto transactionDto) {
    List<ApiTransactionItemDto> itemDtos = transactionDto.items.stream().map(itemAssembler::toDto).collect(toList());
    return new ApiTransactionDto(transactionDto.type, itemDtos, transactionDto.timestamp);
  }
}
