package ca.ulaval.glo4003.domain.portfolio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValue;
import ca.ulaval.glo4003.util.TestStockBuilder;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HistoricalPortfolioTest {
  private final static LocalDate SOME_DATE = LocalDate.of(2018, 11, 16);
  private final static String FIRST_TITLE = "MSFT";
  private final static int FIRST_QTY = 2;
  private final static String SECOND_TITLE = "APPL";
  private final static int SECOND_QTY = 3;
  private final MoneyAmount FIRST_HISTORICAL_VALUE = new MoneyAmount(33.33, Currency.USD);
  private final MoneyAmount SECOND_HISTORICAL_VALUE = new MoneyAmount(44.44, Currency.USD);
  private final HistoricalPortfolio formerPortfolio = new HistoricalPortfolio(SOME_DATE.minusDays(1), new StockCollection());
  private final HistoricalPortfolio latterPortfolio = new HistoricalPortfolio(SOME_DATE, new StockCollection());

  @Mock
  private StockRepository stockRepository;

  @Before
  public void setupHistoricPortfolio() throws StockNotFoundException {
    given(stockRepository.exists(FIRST_TITLE)).willReturn(true);
    given(stockRepository.exists(SECOND_TITLE)).willReturn(true);
    given(stockRepository.findByTitle(FIRST_TITLE)).willReturn(
        new TestStockBuilder().withTitle(FIRST_TITLE).withHistoricalValue(SOME_DATE, StockValue.createOpen(FIRST_HISTORICAL_VALUE)).build());
    given(stockRepository.findByTitle(SECOND_TITLE)).willReturn(
        new TestStockBuilder().withTitle(SECOND_TITLE).withHistoricalValue(SOME_DATE, StockValue.createOpen(SECOND_HISTORICAL_VALUE)).build());
  }

  @Test
  public void givenEmptyPortfolio_whenGetTotal_thenReturn0() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    HistoricalPortfolio emptyPortfolio = new HistoricalPortfolio(SOME_DATE, new StockCollection());

    MoneyAmount total = emptyPortfolio.getTotal(stockRepository);

    assertThat(total).isEqualTo(MoneyAmount.zero(Currency.USD));
  }

  @Test
  public void givenPortfolio_whenGetTotal_thenReturnSumOfHistoricValues() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    StockCollection stockCollection = new StockCollection();
    stockCollection = stockCollection.add(FIRST_TITLE, FIRST_QTY, stockRepository);
    stockCollection = stockCollection.add(SECOND_TITLE, SECOND_QTY, stockRepository);
    HistoricalPortfolio historicalPortfolio = new HistoricalPortfolio(SOME_DATE, stockCollection);

    MoneyAmount total = historicalPortfolio.getTotal(stockRepository);

    assertThat(total).isEqualTo(FIRST_HISTORICAL_VALUE.multiply(FIRST_QTY).add(SECOND_HISTORICAL_VALUE.multiply(SECOND_QTY)));
  }

  @Test
  public void whenCompareToMoreRecentTransaction_thenReturnLessThan0() {
    assertThat(formerPortfolio.compareTo(latterPortfolio)).isLessThan(0);
  }

  @Test
  public void whenCompareToOlderTransaction_thenReturnGreaterThan0() {
    assertThat(latterPortfolio.compareTo(formerPortfolio)).isGreaterThan(0);
  }

  @Test
  public void whenCompareToTransactionWithSameDate_thenReturnZero() {
    assertThat(formerPortfolio.compareTo(formerPortfolio)).isEqualTo(0);
  }
}
