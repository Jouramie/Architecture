package ca.ulaval.glo4003.domain.portfolio;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValue;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.util.TransactionBuilder;
import ca.ulaval.glo4003.util.TransactionItemBuilder;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PortfolioTest {
  private final String SOME_TITLE = "MSFT";
  private final int SOME_QUANTITY = 3;
  private final int SOME_OTHER_QUANTITY = 4;
  private final String SOME_CURRENCY_NAME = "CAD";
  private final BigDecimal SOME_RATE_TO_USD = new BigDecimal(12);
  private final Currency SOME_CURRENCY = new Currency(SOME_CURRENCY_NAME, SOME_RATE_TO_USD);
  private final MoneyAmount SOME_VALUE = new MoneyAmount(12.2, SOME_CURRENCY);
  private final StockValue SOME_STOCK_VALUE = new StockValue(SOME_VALUE, SOME_VALUE, SOME_VALUE);

  @Mock
  private Stock someStock;

  @Mock
  private StockRepository someStockRepository;

  private Portfolio portfolio;

  @Before
  public void setupPortfolio() throws StockNotFoundException {
    portfolio = new Portfolio();

    given(someStock.getValue()).willReturn(SOME_STOCK_VALUE);
    given(someStock.getCurrency()).willReturn(SOME_CURRENCY);
    given(someStock.getTitle()).willReturn(SOME_TITLE);

    given(someStockRepository.exists(SOME_TITLE)).willReturn(true);
    given(someStockRepository.findByTitle(SOME_TITLE)).willReturn(someStock);
  }

  @Test
  public void whenAddStockToPortfolio_thenItCanBeRetrieved() {
    Transaction transaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_QUANTITY).build())
        .build();

    portfolio.add(transaction, someStockRepository);

    assertThat(portfolio.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY);
  }

  @Test
  public void givenStockNotInPortfolio_whenGetQuantity_thenReturnZero() {
    assertThat(portfolio.getQuantity(SOME_TITLE)).isEqualTo(0);
  }

  @Test
  public void givenPortfolioNotEmpty_whenGetCurrentTotalValue_thenReturnSumOfItemValues() throws InvalidStockInPortfolioException {
    Transaction transaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_QUANTITY).build())
        .build();
    portfolio.add(transaction, someStockRepository);

    BigDecimal currentTotal = SOME_VALUE.toUsd().multiply(new BigDecimal(SOME_QUANTITY));
    assertThat(portfolio.getCurrentTotalValue(someStockRepository).getAmount()).isEqualTo(currentTotal);
  }

  @Test
  public void givenPortfolioIsEmpty_whenGetCurrentTotalValue_thenReturnZero() throws InvalidStockInPortfolioException {
    BigDecimal currentTotal = new BigDecimal(0).setScale(2, RoundingMode.HALF_EVEN);
    assertThat(portfolio.getCurrentTotalValue(someStockRepository).getAmount()).isEqualTo(currentTotal);
  }

  @Test
  public void givenPortfolioContainsInvalidStock_whenGetCurrentTotalValue_thenAnExceptionIsThrown()
      throws StockNotFoundException {
    String invalidTitle = "invalid";
    given(someStockRepository.exists(invalidTitle)).willReturn(true);
    given(someStockRepository.findByTitle(invalidTitle)).willThrow(new StockNotFoundException(invalidTitle));
    Transaction transaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(invalidTitle).withQuantity(SOME_QUANTITY).build())
        .build();
    portfolio.add(transaction, someStockRepository);

    assertThatExceptionOfType(InvalidStockInPortfolioException.class).isThrownBy(() -> portfolio.getCurrentTotalValue(someStockRepository));
  }

  @Test
  public void whenGetHistory_thenReturnListWithAHistoricPortfolioPerDay() {
    LocalDate now = LocalDate.now();
    LocalDate from = now.minusDays(15);

    TreeSet<HistoricPortfolio> portfolios = portfolio.getHistory(from, now);

    assertThat(portfolios).hasSize(16);
    List<LocalDate> expectedDates = new ArrayList<>();
    for (int i = 0; i <= 15; ++i) {
      expectedDates.add(now.minusDays(i));
    }
    assertThat(portfolios.stream().map((p) -> p.date)).containsExactlyInAnyOrderElementsOf(expectedDates);
  }

  @Test
  public void givenEmptyHistory_whenGetHistory_thenReturnListWithUnchangedPortfolio() {
    LocalDate now = LocalDate.now();
    LocalDate from = now.minusDays(15);
    Transaction transaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_QUANTITY).build())
        .withTime(from.minusDays(1).atStartOfDay())
        .build();
    portfolio.add(transaction, someStockRepository);

    TreeSet<HistoricPortfolio> portfolios = portfolio.getHistory(from, now);

    assertThat(portfolios.stream().map((p) -> p.stocks)).containsOnly(portfolio.getStocks());
  }

  @Test
  public void givenPortfolioWithFewTransactionsOnDifferentDates_whenGetHistory_thenReturnWithAccurateHistoricalValues() {
    LocalDate now = LocalDate.now();
    Transaction firstTransaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_QUANTITY).build())
        .withTime(now.minusDays(2).atStartOfDay())
        .build();
    portfolio.add(firstTransaction, someStockRepository);
    Transaction secondTransaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_OTHER_QUANTITY).build())
        .withTime(now.minusDays(1).atStartOfDay())
        .build();
    portfolio.add(secondTransaction, someStockRepository);

    TreeSet<HistoricPortfolio> portfolios = portfolio.getHistory(now.minusDays(2), now);

    assertThat(portfolios).hasSize(3);
    Iterator<HistoricPortfolio> it = portfolios.iterator();
    HistoricPortfolio firstPortfolio = it.next();
    HistoricPortfolio secondPortfolio = it.next();
    HistoricPortfolio thirdPortfolio = it.next();

    assertThat(firstPortfolio.stocks.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY);
    assertThat(secondPortfolio.stocks.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY + SOME_OTHER_QUANTITY);
    assertThat(thirdPortfolio.stocks.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY + SOME_OTHER_QUANTITY);
  }

  @Test
  public void givenPortfolioWithFewTransactionsOnSameDates_whenGetHistory_thenReturnWithAccurateHistoricalValues() {
    LocalDate now = LocalDate.now();
    Transaction firstTransaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_QUANTITY).build())
        .withTime(now.minusDays(1).atStartOfDay())
        .build();
    portfolio.add(firstTransaction, someStockRepository);
    Transaction secondTransaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_OTHER_QUANTITY).build())
        .withTime(now.minusDays(1).atStartOfDay().plusHours(1))
        .build();
    portfolio.add(secondTransaction, someStockRepository);

    TreeSet<HistoricPortfolio> portfolios = portfolio.getHistory(now.minusDays(2), now);

    assertThat(portfolios).hasSize(3);
    Iterator<HistoricPortfolio> it = portfolios.iterator();
    HistoricPortfolio firstPortfolio = it.next();
    HistoricPortfolio secondPortfolio = it.next();
    HistoricPortfolio thirdPortfolio = it.next();

    assertThat(firstPortfolio.stocks.getQuantity(SOME_TITLE)).isEqualTo(0);
    assertThat(secondPortfolio.stocks.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY + SOME_OTHER_QUANTITY);
    assertThat(thirdPortfolio.stocks.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY + SOME_OTHER_QUANTITY);
  }
}
