package ca.ulaval.glo4003.service.stock.trend;

import static ca.ulaval.glo4003.domain.stock.StockTrend.*;
import static org.junit.Assert.assertEquals;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.StockTrend;
import ca.ulaval.glo4003.domain.stock.StockValue;
import ca.ulaval.glo4003.domain.stock.StockValueHistory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import org.junit.Test;

public class StockVariationCalculatorTest {

  private static final LocalDate PAST_DATE = LocalDate.of(1984, Month.JANUARY, 24);
  private static final LocalDate CURRENT_DATE = LocalDate.now();
  private static final StockValue LOW_VALUE = new StockValue(new MoneyAmount(1, new Currency("", BigDecimal.ONE)));
  private static final StockValue HIGH_VALUE = new StockValue(new MoneyAmount(100, new Currency("", BigDecimal.ONE)));

  private final StockVariationCalculator calculator = new StockVariationCalculator();

  @Test
  public void givenAPastValueGreaterThanPresentValue_whenGettingTrend_thenReturnDecreasing() {
    StockValueHistory decreasingStock = new StockValueHistory();
    decreasingStock.addValue(PAST_DATE, HIGH_VALUE);
    decreasingStock.addValue(CURRENT_DATE, LOW_VALUE);

    StockTrend trend = calculator.getStockVariationTrendSinceDate(decreasingStock, PAST_DATE);

    assertEquals(DECREASING, trend);
  }

  @Test
  public void givenAPastValueLessThanPresentValue_whenGettingTrend_thenReturnIncreasing() {
    StockValueHistory increasingStock = new StockValueHistory();
    increasingStock.addValue(PAST_DATE, LOW_VALUE);
    increasingStock.addValue(CURRENT_DATE, HIGH_VALUE);

    StockTrend trend = calculator.getStockVariationTrendSinceDate(increasingStock, PAST_DATE);

    assertEquals(INCREASING, trend);
  }

  @Test
  public void givenAPastValueEqualToThePresentValue_whenGettingTrend_thenReturnStable() {
    StockValueHistory stableStock = new StockValueHistory();
    stableStock.addValue(PAST_DATE, HIGH_VALUE);
    stableStock.addValue(CURRENT_DATE, HIGH_VALUE);

    StockTrend trend = calculator.getStockVariationTrendSinceDate(stableStock, PAST_DATE);

    assertEquals(STABLE, trend);
  }

  @Test
  public void givenAMissingPastValue_whenGettingTrend_thenReturnNoData() {
    StockValueHistory stockWithADarkPast = new StockValueHistory();
    stockWithADarkPast.addValue(CURRENT_DATE, LOW_VALUE);

    StockTrend trend = calculator.getStockVariationTrendSinceDate(stockWithADarkPast, PAST_DATE);

    assertEquals(NO_DATA, trend);
  }
}
