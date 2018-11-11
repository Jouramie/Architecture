package ca.ulaval.glo4003.ws.api.cart.assemblers;

import ca.ulaval.glo4003.service.Component;
import ca.ulaval.glo4003.service.cart.dto.TransactionItemDto;
import ca.ulaval.glo4003.ws.api.cart.dto.ApiTransactionItemDto;

@Component
public class ApiTransactionItemAssembler {

  public ApiTransactionItemDto toDto(TransactionItemDto transactionItemDto) {
    return new ApiTransactionItemDto(transactionItemDto.title, transactionItemDto.quantity, transactionItemDto.moneyAmount, transactionItemDto.currency);
  }
}

