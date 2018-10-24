package ca.ulaval.glo4003.service.cart;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.util.TransactionBuilder;
import ca.ulaval.glo4003.util.TransactionItemBuilder;
import ca.ulaval.glo4003.ws.api.cart.ApiTransactionDto;
import ca.ulaval.glo4003.ws.api.cart.ApiTransactionItemDto;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;

public class TransactionAssemblerTest {

  private TransactionItemAssembler itemAssembler;

  private TransactionAssembler assembler;

  @Before
  public void setup() {
    itemAssembler = new TransactionItemAssembler();
    assembler = new TransactionAssembler(itemAssembler);
  }

  @Test
  public void whenToDto_thenTransactionCorrectlyMapped() {
    Transaction transaction = new TransactionBuilder().build();

    TransactionDto resultingDto = assembler.toDto(transaction);

    ApiTransactionItemDto expectedItemDto = buildExpectedItemDto();
    ApiTransactionDto expectedDto = buildExpectedTransactionDto(expectedItemDto);
    assertThat(resultingDto).isEqualToComparingFieldByFieldRecursively(expectedDto);
  }

  private ApiTransactionItemDto buildExpectedItemDto() {
    return new ApiTransactionItemDto(TransactionItemBuilder.DEFAULT_STOCK_ID,
        TransactionItemBuilder.DEFAULT_QUANTITY,
        TransactionItemBuilder.DEFAULT_AMOUNT.getAmount(),
        TransactionItemBuilder.DEFAULT_CURRENCY.getName());
  }

  private ApiTransactionDto buildExpectedTransactionDto(ApiTransactionItemDto expectedItemDto) {
    return new ApiTransactionDto(TransactionBuilder.DEFAULT_TYPE.toString(),
        Collections.singletonList(expectedItemDto),
        TransactionBuilder.DEFAULT_TIME);
  }
}