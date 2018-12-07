package ca.ulaval.glo4003.ws.api.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.service.cart.dto.TransactionDto;
import ca.ulaval.glo4003.service.transaction.TransactionService;
import ca.ulaval.glo4003.ws.api.transaction.dto.TransactionModelDto;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransactionRessourceTest {

  @Mock
  List<TransactionDto> serviceDtos;
  @Mock
  TransactionModelDto expectedDtos;
  @Mock
  private TransactionService transactionService;
  private TransactionResource transactionResource;

  @Before
  public void setUp() {
    given(transactionService.getAllTransactions()).willReturn(serviceDtos);
    transactionResource = new TransactionResource();
  }

  @Test
  public void whenGetTransactions_thenReturnAllTransactionsForAllUsers() {
    List<TransactionModelDto> resultingTransactionsDtos = transactionResource.getTransactions("since");
    assertThat(resultingTransactionsDtos.get(0)).isSameAs(expectedDtos);
  }
}
