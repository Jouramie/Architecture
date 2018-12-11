package ca.ulaval.glo4003.domain.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.exceptions.WrongRoleException;
import ca.ulaval.glo4003.service.date.SinceParameter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransactionRetrieverTest {
  private static final LocalDateTime SOME_TIMESTAMP = LocalDateTime.now();
  private static final LocalDate SOME_TO_DATE = SOME_TIMESTAMP.toLocalDate();
  private static final LocalDate SOME_FROM_DATE = SOME_TO_DATE.minusDays(SinceParameter.LAST_FIVE_DAYS.toDays());
  private static final String SOME_EMAIL = "email@email.com";
  private static final String SOME_TITLE = "some_title";

  private static final TransactionItem SOME_TRANSACTION_ITEM = new TransactionItemBuilder().withTitle(SOME_TITLE).build();
  private static final Transaction FIRST_TRANSACTION = new TransactionBuilder().withItem(SOME_TRANSACTION_ITEM).withTime(SOME_TIMESTAMP.minusDays(1)).build();
  private static final Transaction SECOND_TRANSACTION = new TransactionBuilder().withTime(SOME_TIMESTAMP.minusDays(3)).build();
  private static final Transaction THIRD_TRANSACTION_LATER = new TransactionBuilder().withTime(SOME_TIMESTAMP.minusDays(6)).build();
  private static final List<Transaction> SOME_TRANSACTION_INVESTOR = Arrays.asList(FIRST_TRANSACTION);
  private static final List<Transaction> OTHER_TRANSACTION_INVESTOR = Arrays.asList(SECOND_TRANSACTION, THIRD_TRANSACTION_LATER);

  private TransactionRetriever transactionRetriever;

  @Mock
  private Investor some_investor;
  @Mock
  private Investor other_investor;
  @Mock
  private UserRepository userRepository;

  @Before
  public void setUp() {
    List<Investor> investors = new ArrayList<>();
    investors.add(some_investor);
    investors.add(other_investor);

    given(userRepository.findInvestors()).willReturn(investors);
    given(some_investor.getTransactions(SOME_FROM_DATE.atStartOfDay(), SOME_TO_DATE.atStartOfDay()))
        .willReturn(SOME_TRANSACTION_INVESTOR);
    given(other_investor.getTransactions(SOME_FROM_DATE.atStartOfDay(), SOME_TO_DATE.atStartOfDay()))
        .willReturn(OTHER_TRANSACTION_INVESTOR);

    transactionRetriever = new TransactionRetriever(userRepository);
  }

  @Test
  public void whenGetTransactions_thenReturnTransactionsBetweenDates() {
    List<Transaction> resultingTransactions = transactionRetriever.getTransactions(SOME_FROM_DATE, SOME_TO_DATE);

    assertThat(resultingTransactions).containsExactly(FIRST_TRANSACTION, SECOND_TRANSACTION);
  }

  @Test
  public void whenGetTransactionsByEmail_thenReturnTransactionsForSpecificUser()
      throws UserNotFoundException, WrongRoleException {
    given(userRepository.findByEmail(SOME_EMAIL, Investor.class)).willReturn(other_investor);

    List<Transaction> resultingTransactions = transactionRetriever
        .getTransactionsByEmail(SOME_EMAIL, SOME_FROM_DATE, SOME_TO_DATE);

    assertThat(resultingTransactions).containsExactly(SECOND_TRANSACTION);
  }

  @Test
  public void whenGetTransactionsByTitle_thenReturnTransactionsForASpecificStock() {
    List<Transaction> resultingTransactions = transactionRetriever
        .getTransactionsByTitle(SOME_TITLE, SOME_FROM_DATE, SOME_TO_DATE);

    assertThat(resultingTransactions).containsExactly(FIRST_TRANSACTION);
  }
}
