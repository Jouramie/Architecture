package ca.ulaval.glo4003.ws.api.transaction;

import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.service.transaction.TransactionService;
import ca.ulaval.glo4003.ws.api.transaction.dto.TransactionModelDto;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransactionRessourceTest {

  @Mock
  private TransactionService transactionService;
  @Mock
  TransactionModelDto serviceDto;

  private TransactionResource transactionResource;
  //@Before
 /* public void setUp(){
    given(transactionService.getTransactions).willReturn(Collections.singletonList(serviceDto));
    transactionResource = new TransactionResource();
  }
  @Test
  public void whenGetTransactions_thenReturnAllTransactionsForAllUsers(){
    List<TransactionModelDto> resultingTransactionsDtos = transactionResource.getTransactions();
    assertThat(resultingTransactionsDtos.get(0)).isEqualsTo(expextedDto);
  }*/
}
