package ca.ulaval.glo4003.domain.transaction;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.exception.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.exception.WrongRoleException;
import ca.ulaval.glo4003.service.date.SinceParameter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransactionRetrieverTest {
  private static final LocalDateTime SOME_TO_DATE = LocalDateTime.now().toLocalDate().atStartOfDay();
  private static final LocalDateTime SOME_FROM_DATE = SOME_TO_DATE.minusDays(SinceParameter.LAST_FIVE_DAYS.toDays());
  private static final String SOME_EMAIL = "email@email.com";
  private static final String SOME_TITLE = "some_title";

  private TransactionRetriever transactionRetriever;

  @Mock
  private Investor some_investor;
  @Mock
  private Investor other_investor;
  @Mock
  private UserRepository userRepository;
  @Mock
  private Transaction first_transaction;
  @Mock
  private Transaction second_transaction;

  @Before
  public void setUp() {
    given(first_transaction.containsTitle(SOME_TITLE)).willReturn(true);
    given(second_transaction.containsTitle(SOME_TITLE)).willReturn(false);

    given(userRepository.findInvestors()).willReturn(Arrays.asList(some_investor, other_investor));
    given(some_investor.getTransactions(SOME_FROM_DATE, SOME_TO_DATE)).willReturn(singletonList(first_transaction));
    given(other_investor.getTransactions(SOME_FROM_DATE, SOME_TO_DATE)).willReturn(singletonList(second_transaction));

    transactionRetriever = new TransactionRetriever(userRepository);
  }

  @Test
  public void whenGetTransactions_thenReturnTransactionsBetweenDates() {
    List<Transaction> resultingTransactions = transactionRetriever.getTransactions(SOME_FROM_DATE.toLocalDate(), SOME_TO_DATE.toLocalDate());

    assertThat(resultingTransactions).containsExactly(first_transaction, second_transaction);
  }

  @Test
  public void whenGetTransactionsByEmail_thenReturnTransactionsForSpecificUser()
      throws UserNotFoundException, WrongRoleException {
    given(userRepository.findByEmail(SOME_EMAIL, Investor.class)).willReturn(other_investor);

    List<Transaction> resultingTransactions = transactionRetriever
        .getTransactionsByEmail(SOME_EMAIL, SOME_FROM_DATE.toLocalDate(), SOME_TO_DATE.toLocalDate());

    assertThat(resultingTransactions).containsExactly(second_transaction);
  }

  @Test
  public void whenGetTransactionsByTitle_thenReturnTransactionsForASpecificStock() {
    List<Transaction> resultingTransactions = transactionRetriever
        .getTransactionsByTitle(SOME_TITLE, SOME_FROM_DATE.toLocalDate(), SOME_TO_DATE.toLocalDate());

    assertThat(resultingTransactions).containsExactly(first_transaction);
  }
}
