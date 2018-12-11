package ca.ulaval.glo4003.ws.api.transaction.assemblers;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.service.cart.dto.TransactionDto;
import ca.ulaval.glo4003.service.cart.dto.TransactionItemDto;
import ca.ulaval.glo4003.ws.api.transaction.dto.ApiTransactionDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ApiTransactionAssemblerTest {
  private static final String SOME_TRANSACTION_TYPE = "PURCHASE";
  private static final List<TransactionItemDto> SOME_ITEMS = new ArrayList<>();
  private static final LocalDateTime SOME_TIMESTAMP = LocalDateTime.now();
  private final ApiTransactionAssembler transactionAssembler = new ApiTransactionAssembler(new ApiTransactionItemAssembler());
  TransactionDto serviceDto = new TransactionDto(SOME_TRANSACTION_TYPE, SOME_ITEMS, SOME_TIMESTAMP);

  @Test
  public void whenToDto_thenTransactionIsMappedCorrectly() {
    ApiTransactionDto resultingDto = transactionAssembler.toDto(serviceDto);

    assertThat(resultingDto.type).isEqualTo(serviceDto.type);
    assertThat(resultingDto.items.size()).isEqualTo(serviceDto.items.size());
    assertThat(resultingDto.timestamp).isEqualTo(serviceDto.timestamp);
  }
}
