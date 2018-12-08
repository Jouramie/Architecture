package ca.ulaval.glo4003.domain.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import ca.ulaval.glo4003.service.date.SinceParameter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransactionNoNameTest {
  private static final SinceParameter SOME_SINCE_PARAMETER = SinceParameter.LAST_FIVE_DAYS;
  private static final LocalDateTime SOME_TIMESTAMP = LocalDateTime.now();
  private static final LocalDate SOME_CURRENT_DATE = SOME_TIMESTAMP.toLocalDate();
  private static final LocalDate SOME_FROM_DATE = SOME_CURRENT_DATE.minusDays(SOME_SINCE_PARAMETER.toDays());
  private static final String SOME_EMAIL = "email@email.com";
  private static final String SOME_TITLE = "title";

  private TransactionNoName transactionNoName;
  private List<Investor> investors;
  @Mock
  private Investor some_investor;
  @Mock
  private Investor other_investor;
  @Mock
  private UserRepository userRepository;

  @Before
  public void setUp() {

    investors = new ArrayList<>();
    investors.add(some_investor);
    investors.add(other_investor);

    given(userRepository.findInvestor()).willReturn(investors);

    transactionNoName = new TransactionNoName(userRepository);
  }

  @Test
  public void whenGetTransactions_thenReturnAllTransactions() {
    Transaction firstTransaction = new TransactionBuilder().withTime(SOME_TIMESTAMP.minusDays(1)).build();
    Transaction secondTransaction = new TransactionBuilder().withTime(SOME_TIMESTAMP.minusDays(3)).build();
    Transaction thirdTransaction = new TransactionBuilder().withTime(SOME_TIMESTAMP).build();

    List<Transaction> transactionsSomeInvestor = new ArrayList<>();
    transactionsSomeInvestor.add(firstTransaction);
    transactionsSomeInvestor.add(secondTransaction);

    List<Transaction> transactionsOtherInvestor = new ArrayList<>();
    transactionsOtherInvestor.add(thirdTransaction);

    given(some_investor.getTransactions()).willReturn(transactionsSomeInvestor);
    given(other_investor.getTransactions()).willReturn(transactionsOtherInvestor);

    List<Transaction> resultingTransactions = transactionNoName.getTransactions(SOME_FROM_DATE, SOME_CURRENT_DATE);

    assertThat(resultingTransactions).containsExactly(firstTransaction, secondTransaction, thirdTransaction);
  }

  @Test
  public void whenGetTransactionsByEmail_thenReturnTrnsactionsForSpecificUser() throws UserNotFoundException {
    given(userRepository.findByEmail(SOME_EMAIL)).willReturn(some_investor);

    Transaction firstTransaction = new TransactionBuilder().withTime(SOME_TIMESTAMP.minusDays(1)).build();
    Transaction secondTransaction = new TransactionBuilder().withTime(SOME_TIMESTAMP.minusDays(6)).build();

    List<Transaction> transactionsSomeInvestor = new ArrayList<>();
    transactionsSomeInvestor.add(firstTransaction);
    transactionsSomeInvestor.add(secondTransaction);

    given(some_investor.getTransactions()).willReturn(transactionsSomeInvestor);

    List<Transaction> resultingTransactions = transactionNoName.getTransactionsByEmail(SOME_EMAIL, SOME_FROM_DATE, SOME_CURRENT_DATE);
    assertThat(resultingTransactions).containsExactly(firstTransaction);
  }

  @Test
  public void whenGetTransactionsByTitlle_thenReturnTransactionsForASpecificStock() {
    //given(userRepository.)
    //List<Transaction> resultingTransactions = transactionNoName.getTransactionsByTitle(SOME_TITLE, SOME_FROM_DATE, SOME_CURRENT_DATE);
  }
}
