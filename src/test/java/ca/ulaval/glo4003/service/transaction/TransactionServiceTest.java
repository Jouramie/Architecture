package ca.ulaval.glo4003.service.transaction;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.transaction.TransactionNoName;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import ca.ulaval.glo4003.service.cart.assemblers.TransactionAssembler;
import ca.ulaval.glo4003.service.date.DateService;
import ca.ulaval.glo4003.service.date.SinceParameter;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {
  private static final SinceParameter SOME_SINCE_PARAMETER = SinceParameter.LAST_FIVE_DAYS;
  private static final LocalDate SOME_CURRENT_DATE = LocalDate.now();
  private static final LocalDate SOME_FROM_DATE = SOME_CURRENT_DATE.minusDays(SOME_SINCE_PARAMETER.toDays());
  private static final String SOME_EMAIL = "email@email.com";
  private static final String SOME_TITLE = "title";
  @Mock
  private TransactionAssembler transactionAssembler;
  @Mock
  private TransactionNoName transactionNoName;
  @Mock
  private DateService dateService;
  private TransactionService transactionService;

  @Before
  public void setup() {
    given(dateService.getCurrentDate()).willReturn(SOME_CURRENT_DATE);
    given(dateService.getDateSince(SOME_SINCE_PARAMETER)).willReturn(SOME_FROM_DATE);
    transactionService = new TransactionService(transactionAssembler, transactionNoName, dateService);
  }

  @Test
  public void whenGetAllTransactions_thenTransactionBetweenFromAndCurrentAreGotten() {
    transactionService.getAllTransactions(SOME_SINCE_PARAMETER);

    verify(transactionNoName).getTransactions(SOME_FROM_DATE, SOME_CURRENT_DATE);
  }

  @Test
  public void whenGetTransactionsByEmail_thenTransactionByEmailBetweenFromAndCurrentAreGotten() throws UserNotFoundException {
    transactionService.getTransactionsByEmail(SOME_EMAIL, SOME_SINCE_PARAMETER);

    verify(transactionNoName).getTransactionsByEmail(SOME_EMAIL, SOME_FROM_DATE, SOME_CURRENT_DATE);
  }

  @Test
  public void whenGetTransactionsByTitle_thenTransactionByTitleBetweenFromAndCurrentAreGotten() {
    transactionService.getTransactionsByTitle(SOME_TITLE, SOME_SINCE_PARAMETER);

    verify(transactionNoName).getTransactionsByTitle(SOME_TITLE, SOME_FROM_DATE, SOME_CURRENT_DATE);
  }
}