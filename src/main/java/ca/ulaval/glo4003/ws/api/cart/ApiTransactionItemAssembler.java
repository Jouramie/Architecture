package ca.ulaval.glo4003.ws.api.cart;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.service.cart.TransactionItemDto;

@Component
public class ApiTransactionItemAssembler {

  public ApiTransactionItemDto toDto(TransactionItemDto transactionItemDto) {
    return new ApiTransactionItemDto(transactionItemDto.title, transactionItemDto.quantity, transactionItemDto.moneyAmount, transactionItemDto.currency);
  }
}

