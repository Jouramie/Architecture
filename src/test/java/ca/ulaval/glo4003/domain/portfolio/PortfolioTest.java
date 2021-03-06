package ca.ulaval.glo4003.domain.portfolio;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValue;
import ca.ulaval.glo4003.domain.stock.StockValueBuilder;
import ca.ulaval.glo4003.domain.stock.exception.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionBuilder;
import ca.ulaval.glo4003.domain.transaction.TransactionItemBuilder;
import ca.ulaval.glo4003.infrastructure.stock.InMemoryStockRepository;
import ca.ulaval.glo4003.util.TestStockBuilder;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.LongStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PortfolioTest {
  private final String SOME_TITLE = "MSFT";
  private final String SOME_OTHER_TITLE = "AAPL";
  private final int SOME_QUANTITY = 3;
  private final int SOME_OTHER_QUANTITY = 4;
  private final String SOME_CURRENCY_NAME = "CAD";
  private final BigDecimal SOME_RATE_TO_USD = new BigDecimal(12);
  private final Currency SOME_CURRENCY = new Currency(SOME_CURRENCY_NAME, SOME_RATE_TO_USD);
  private final MoneyAmount SOME_VALUE = new MoneyAmount(12.2, SOME_CURRENCY);
  private final StockValue SOME_STOCK_VALUE = new StockValueBuilder().withLatestValue(SOME_VALUE).build();
  private final LocalDate NOW = LocalDate.now();

  private Stock someStock;
  private Stock someOtherStock;

  private StockRepository someStockRepository;

  private Portfolio portfolio;

  @Before
  public void setupPortfolio() {
    portfolio = new Portfolio();

    someStock = new TestStockBuilder()
        .withTitle(SOME_TITLE)
        .withCloseValue(SOME_VALUE)
        .build();

    someOtherStock = new TestStockBuilder()
        .withTitle(SOME_OTHER_TITLE)
        .withCloseValue(SOME_VALUE)
        .build();

    someStockRepository = new InMemoryStockRepository();
    someStockRepository.add(someStock);
    someStockRepository.add(someOtherStock);
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
  public void givenPortfolioNotEmpty_whenGetCurrentTotalValue_thenReturnSumOfItemValues()
      throws InvalidStockInPortfolioException {
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
  public void whenGetHistory_thenReturnListWithAHistoricPortfolioPerDay() {
    int numDays = 15;

    LocalDate from = NOW.minusDays(numDays);

    TreeSet<HistoricalPortfolio> portfolios = portfolio.getHistory(from, NOW);

    assertThat(portfolios).hasSize(numDays + 1);
    List<LocalDate> expectedDates = LongStream.rangeClosed(0, numDays).mapToObj(NOW::minusDays).collect(toList());
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

    TreeSet<HistoricalPortfolio> portfolios = portfolio.getHistory(from, now);

    assertThat(portfolios.stream().map((p) -> p.stocks)).containsOnly(portfolio.getStocks());
  }

  @Test
  public void givenPortfolioWithFewTransactionsOnDifferentDates_whenGetHistory_thenReturnWithAccurateHistoricalValues() {
    setupPortfolioWithTransactionsOnDifferentDates();

    TreeSet<HistoricalPortfolio> portfolios = portfolio.getHistory(NOW.minusDays(2), NOW);

    int[] stockQuantities = portfolios.stream().mapToInt(p -> p.stocks.getQuantity(SOME_TITLE)).toArray();
    assertThat(stockQuantities).containsExactly(SOME_QUANTITY,
        SOME_QUANTITY + SOME_OTHER_QUANTITY,
        SOME_QUANTITY + SOME_OTHER_QUANTITY);
  }

  @Test
  public void givenPortfolioWithFewTransactionsOnSameDates_whenGetHistory_thenReturnWithAccurateHistoricalValues() {
    setupPortfolioWithTransactionsOnSameDate(NOW.minusDays(1));

    TreeSet<HistoricalPortfolio> portfolios = portfolio.getHistory(NOW.minusDays(2), NOW);

    int[] stockQuantities = portfolios.stream().mapToInt(p -> p.stocks.getQuantity(SOME_TITLE)).toArray();
    assertThat(stockQuantities).containsExactly(0,
        SOME_QUANTITY + SOME_OTHER_QUANTITY,
        SOME_QUANTITY + SOME_OTHER_QUANTITY);
  }

  @Test
  public void givenStocksInPortfolio_whenGetMostIncreasingStock_thenMostIncreasingStockIsReturned()
      throws NoStockValueFitsCriteriaException, InvalidStockInPortfolioException {
    setupPortfolioWithDifferentStocksOnSameDate(NOW.minusDays(5));
    setupStockWithLowestVariation(NOW.minusDays(5));
    setupStockWithHighestVariation(NOW.minusDays(5));

    String mostIncreasingStockTitle = portfolio.getMostIncreasingStockTitle(NOW.minusDays(5), someStockRepository);

    assertThat(mostIncreasingStockTitle).isEqualTo(SOME_OTHER_TITLE);
  }

  @Test
  public void givenEmptyPortfolio_whenGetMostIncreasingStock_thenReturnNull()
      throws NoStockValueFitsCriteriaException, InvalidStockInPortfolioException {
    String mostIncreasingStockTitle = portfolio.getMostIncreasingStockTitle(NOW.minusDays(5), someStockRepository);

    assertThat(mostIncreasingStockTitle).isNull();
  }

  @Test
  public void givenStocksInPortfolio_whenGetMostDecreasingStock_thenMostDecreasingStockIsReturned()
      throws NoStockValueFitsCriteriaException, InvalidStockInPortfolioException {
    setupPortfolioWithDifferentStocksOnSameDate(NOW.minusDays(5));
    setupStockWithLowestVariation(NOW.minusDays(5));
    setupStockWithHighestVariation(NOW.minusDays(5));

    String mostDecreasingStockTitle = portfolio.getMostDecreasingStockTitle(NOW.minusDays(5), someStockRepository);

    assertThat(mostDecreasingStockTitle).isEqualTo(SOME_TITLE);
  }

  @Test
  public void givenEmptyPortfolio_whenGetMostDecreasingStock_thenReturnNull()
      throws NoStockValueFitsCriteriaException, InvalidStockInPortfolioException {
    String mostDecreasingStockTitle = portfolio.getMostDecreasingStockTitle(NOW.minusDays(5), someStockRepository);

    assertThat(mostDecreasingStockTitle).isNull();
  }

  @Test
  public void whenGetTransactions_thenReturnTransactionsBetweenDates() {
    LocalDateTime from = NOW.atStartOfDay();
    LocalDateTime to = NOW.plusDays(1).atStartOfDay();

    Transaction beforeTransaction = new TransactionBuilder().withTime(NOW.minusDays(10).atStartOfDay()).build();
    Transaction firstTransaction = new TransactionBuilder().withTime(from).build();
    Transaction secondTransaction = new TransactionBuilder().withTime(to).build();
    Transaction afterTransaction = new TransactionBuilder().withTime(NOW.plusDays(10).atStartOfDay()).build();
    portfolio.add(beforeTransaction, someStockRepository);
    portfolio.add(firstTransaction, someStockRepository);
    portfolio.add(secondTransaction, someStockRepository);
    portfolio.add(afterTransaction, someStockRepository);

    List<Transaction> transactions = portfolio.getTransactions(from, to);

    assertThat(transactions).containsExactly(firstTransaction, secondTransaction);
  }

  private void setupPortfolioWithTransactionsOnDifferentDates() {
    Transaction firstTransaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_QUANTITY).build())
        .withTime(NOW.minusDays(2).atStartOfDay())
        .build();
    portfolio.add(firstTransaction, someStockRepository);
    Transaction secondTransaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_OTHER_QUANTITY).build())
        .withTime(NOW.minusDays(1).atStartOfDay())
        .build();
    portfolio.add(secondTransaction, someStockRepository);
  }

  private void setupPortfolioWithTransactionsOnSameDate(LocalDate date) {
    Transaction firstTransaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_QUANTITY).build())
        .withTime(date.atStartOfDay())
        .build();
    portfolio.add(firstTransaction, someStockRepository);
    Transaction secondTransaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_OTHER_QUANTITY).build())
        .withTime(date.atStartOfDay().plusHours(1))
        .build();
    portfolio.add(secondTransaction, someStockRepository);
  }

  private void setupPortfolioWithDifferentStocksOnSameDate(LocalDate date) {
    Transaction firstTransaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_QUANTITY).build())
        .withTime(date.atStartOfDay())
        .build();
    portfolio.add(firstTransaction, someStockRepository);
    Transaction secondTransaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_OTHER_TITLE).withQuantity(SOME_QUANTITY).build())
        .withTime(date.atStartOfDay())
        .build();
    portfolio.add(secondTransaction, someStockRepository);
  }

  private void setupStockWithLowestVariation(LocalDate from) {
    someStock.getValueHistory().addValue(from, SOME_STOCK_VALUE);
  }

  private void setupStockWithHighestVariation(LocalDate from) {
    MoneyAmount someOtherValue = new MoneyAmount(SOME_VALUE.getAmount().multiply(new BigDecimal(0.5)), SOME_CURRENCY);
    StockValue someOtherStockValue = new StockValueBuilder().withAllValue(someOtherValue).build();
    someOtherStock.getValueHistory().addValue(from, someOtherStockValue);
  }
}
