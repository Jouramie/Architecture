package ca.ulaval.glo4003.ws.api.transaction.assembler;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.service.transaction.TransactionItemDto;
import ca.ulaval.glo4003.ws.api.transaction.dto.ApiTransactionItemDto;

@Component
public class ApiTransactionItemAssembler {

  public ApiTransactionItemDto toDto(TransactionItemDto transactionItemDto) {
    return new ApiTransactionItemDto(transactionItemDto.title, transactionItemDto.quantity, transactionItemDto.moneyAmount, transactionItemDto.currency);
  }
}

