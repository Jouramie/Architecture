package ca.ulaval.glo4003.service.cart;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionBuilder;
import ca.ulaval.glo4003.domain.transaction.TransactionItemBuilder;
import ca.ulaval.glo4003.service.cart.assemblers.TransactionAssembler;
import ca.ulaval.glo4003.service.cart.assemblers.TransactionItemAssembler;
import ca.ulaval.glo4003.service.cart.dto.TransactionDto;
import ca.ulaval.glo4003.service.cart.dto.TransactionItemDto;
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
    Transaction transaction = new TransactionBuilder().withDefaultItems().build();

    TransactionDto resultingDto = assembler.toDto(transaction);

    TransactionItemDto expectedItemDto = buildExpectedItemDto();
    TransactionDto expectedDto = buildExpectedTransactionDto(expectedItemDto);
    assertThat(resultingDto).isEqualToComparingFieldByFieldRecursively(expectedDto);
  }

  private TransactionItemDto buildExpectedItemDto() {
    return new TransactionItemDto(TransactionItemBuilder.DEFAULT_TITLE,
        TransactionItemBuilder.DEFAULT_QUANTITY,
        TransactionItemBuilder.DEFAULT_AMOUNT.getAmount(),
        TransactionItemBuilder.DEFAULT_CURRENCY.getName());
  }

  private TransactionDto buildExpectedTransactionDto(TransactionItemDto expectedItemDto) {
    return new TransactionDto(TransactionBuilder.DEFAULT_TYPE.toString(),
        Collections.singletonList(expectedItemDto),
        TransactionBuilder.DEFAULT_TIME);
  }
}