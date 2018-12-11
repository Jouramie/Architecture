package ca.ulaval.glo4003.ws.api.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.service.cart.dto.TransactionDto;
import ca.ulaval.glo4003.service.date.SinceParameter;
import ca.ulaval.glo4003.service.transaction.TransactionService;
import ca.ulaval.glo4003.util.TransactionDtoBuilder;
import ca.ulaval.glo4003.ws.api.transaction.assemblers.ApiTransactionAssembler;
import ca.ulaval.glo4003.ws.api.transaction.assemblers.ApiTransactionItemAssembler;
import ca.ulaval.glo4003.ws.api.transaction.dto.ApiTransactionDto;
import ca.ulaval.glo4003.ws.api.util.SinceParameterConverter;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserTransactionResourceTest {

  private static final String SOME_EMAIL = "email@email.com";
  private static final String SOME_SINCE_VALUE_PARAMETER = "LAST_FIVE_DAYS";
  private static final SinceParameter SOME_SINCE_PARAMETER = SinceParameter.LAST_FIVE_DAYS;
  private static final List<TransactionDto> SERVICE_DTOS = new TransactionDtoBuilder().buildList();

  private final ApiTransactionAssembler transactionAssembler = new ApiTransactionAssembler(new ApiTransactionItemAssembler());
  private final SinceParameterConverter sinceParameterConverter = new SinceParameterConverter();
  @Mock
  private TransactionService transactionService;
  private UserTransactionResource transactionResource;

  @Before
  public void setUp() {
    given(transactionService.getTransactionsByEmail(SOME_EMAIL, SOME_SINCE_PARAMETER)).willReturn(SERVICE_DTOS);
    transactionResource = new UserTransactionResource(transactionService, transactionAssembler, sinceParameterConverter);
  }

  @Test
  public void whenGetTransactions_thenTransactionAreGotten() {
    transactionResource.getUserTransactions(SOME_EMAIL, SOME_SINCE_VALUE_PARAMETER);

    verify(transactionService).getTransactionsByEmail(SOME_EMAIL, SOME_SINCE_PARAMETER);
  }

  @Test
  public void whenGetTransactions_thenReturnAllTransactions() {
    List<ApiTransactionDto> resultingTransactionsDtos = transactionResource.getUserTransactions(SOME_EMAIL, SOME_SINCE_VALUE_PARAMETER);

    assertThat(resultingTransactionsDtos.size()).isEqualTo(SERVICE_DTOS.size());
  }
}
