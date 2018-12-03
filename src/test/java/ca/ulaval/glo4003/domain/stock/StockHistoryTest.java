package ca.ulaval.glo4003.domain.stock;

import static ca.ulaval.glo4003.domain.stock.StockTrend.DECREASING;
import static ca.ulaval.glo4003.domain.stock.StockTrend.INCREASING;
import static ca.ulaval.glo4003.domain.stock.StockTrend.NO_DATA;
import static ca.ulaval.glo4003.domain.stock.StockTrend.STABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.ulaval.glo4003.domain.money.MoneyAmountUtil;
import java.time.LocalDate;
import java.time.Month;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;

public class StockHistoryTest {
  private static final LocalDate SOME_DATE = LocalDate.now();
  private static final LocalDate SOME_OLDER_DATE = SOME_DATE.minusDays(1);
  private static final LocalDate SOME_MORE_RECENT_DATE = SOME_DATE.plusDays(1);
  private static final StockValue SOME_VALUE = new StockValue(MoneyAmountUtil.of(12.34));
  private static final StockValue SOME_OTHER_VALUE = new StockValue(MoneyAmountUtil.of(45.67));
  private static final StockValue SOME_LOWER_VALUE = new StockValue(MoneyAmountUtil.of(1));
  private static final StockValue SOME_HIGHER_VALUE = new StockValue(MoneyAmountUtil.of(300.00));
  private static final StockValue SOME_EVEN_HIGHER_VALUE = new StockValue(MoneyAmountUtil.of(500.00));
  private static final LocalDate START_DATE = LocalDate.of(2018, 9, 12);
  private static final LocalDate END_DATE = LocalDate.of(2018, 10, 12);
  private static final LocalDate MIDDLE_DATE = LocalDate.of(2018, 10, 1);

  private StockHistory history;

  @Before
  public void setup() {
    history = new StockHistory();
  }

  @Test
  public void whenAddValue_thenAddValueToTheHistory() {
    history.addValue(SOME_DATE, SOME_VALUE);

    HistoricalStockValue expectedHistoricalValue = new HistoricalStockValue(SOME_DATE, SOME_VALUE);
    assertThat(history.getAllStoredValues()).hasSize(1);
    assertThat(history.getAllStoredValues()).first().isEqualToComparingFieldByField(expectedHistoricalValue);
  }

  @Test
  public void givenHistoryWithTwoValues_whenGetLatestValue_thenReturnMostRecentValue() {
    history.addValue(SOME_DATE, SOME_VALUE);
    history.addValue(SOME_MORE_RECENT_DATE, SOME_OTHER_VALUE);

    HistoricalStockValue result = history.getLatestValue();

    assertThat(result.date).isEqualTo(SOME_MORE_RECENT_DATE);
    assertThat(result.value).isEqualTo(SOME_OTHER_VALUE);
  }

  @Test
  public void givenHistoryWithSingleValue_whenAddNextValue_thenAddValueWithDateSetToNextDay() {
    history.addValue(SOME_DATE, SOME_VALUE);

    history.addNextValue(SOME_OTHER_VALUE);

    HistoricalStockValue result = history.getLatestValue();
    assertThat(result.date).isEqualTo(SOME_DATE.plusDays(1));
    assertThat(result.value).isEqualTo(SOME_OTHER_VALUE);
  }

  @Test
  public void givenHistoryWithMultipleValues_whenGetMaxValue_thenReturnMaxValueInInterval()
      throws NoStockValueFitsCriteriaException {
    history.addValue(START_DATE.minusDays(1), SOME_EVEN_HIGHER_VALUE);
    history.addValue(START_DATE, SOME_VALUE);
    history.addValue(MIDDLE_DATE, SOME_HIGHER_VALUE);
    history.addValue(END_DATE, SOME_VALUE);
    history.addValue(END_DATE.plusDays(1), SOME_EVEN_HIGHER_VALUE);

    HistoricalStockValue result = history.getMaxValue(START_DATE, END_DATE);

    assertThat(result.date).isSameAs(MIDDLE_DATE);
    assertThat(result.value).isSameAs(SOME_HIGHER_VALUE);
  }

  @Test
  public void givenHistoryWithMaxValueOnStartDate_whenGetMaxValue_thenReturnMaxValueInInterval()
      throws NoStockValueFitsCriteriaException {
    history.addValue(START_DATE.minusDays(1), SOME_EVEN_HIGHER_VALUE);
    history.addValue(START_DATE, SOME_HIGHER_VALUE);
    history.addValue(MIDDLE_DATE, SOME_VALUE);
    history.addValue(END_DATE, SOME_VALUE);
    history.addValue(END_DATE.plusDays(1), SOME_EVEN_HIGHER_VALUE);

    HistoricalStockValue result = history.getMaxValue(START_DATE, END_DATE);

    assertThat(result.date).isSameAs(START_DATE);
    assertThat(result.value).isSameAs(SOME_HIGHER_VALUE);
  }

  @Test
  public void givenHistoryWithMaxValueOnEndDate_whenGetMaxValue_thenReturnMaxValueInInterval()
      throws NoStockValueFitsCriteriaException {
    history.addValue(START_DATE.minusDays(1), SOME_EVEN_HIGHER_VALUE);
    history.addValue(START_DATE, SOME_VALUE);
    history.addValue(MIDDLE_DATE, SOME_VALUE);
    history.addValue(END_DATE, SOME_HIGHER_VALUE);
    history.addValue(END_DATE.plusDays(1), SOME_EVEN_HIGHER_VALUE);

    HistoricalStockValue result = history.getMaxValue(START_DATE, END_DATE);

    assertThat(result.date).isSameAs(END_DATE);
    assertThat(result.value).isSameAs(SOME_HIGHER_VALUE);
  }

  @Test
  public void givenEmptyHistory_whenGetMaxValue_thenExceptionIsThrow() {
    ThrowingCallable getMaxValue = () -> history.getMaxValue(START_DATE, END_DATE);

    assertThatThrownBy(getMaxValue).isInstanceOf(NoStockValueFitsCriteriaException.class);
  }

  @Test
  public void givenNoData_whenGettingValueOnASpecificDay_thenExceptionIsThrow() {
    LocalDate missingDate = LocalDate.of(1970, Month.JANUARY, 1);

    ThrowingCallable getValue = () -> history.getValueOnDay(missingDate);

    assertThatThrownBy(getValue);
  }

  @Test
  public void whenGettingValueOnASpecificDay_thenReturnStockValueOnThatDay()
      throws NoStockValueFitsCriteriaException {
    history.addValue(SOME_DATE, SOME_VALUE);

    StockValue valueOnDay = history.getValueOnDay(SOME_DATE);

    assertThat(valueOnDay).isSameAs(SOME_VALUE);
  }

  @Test
  public void givenNoDataOnRequestedDay_whenGettingValueOnASpecificDay_thenReturnPreviousValue()
      throws NoStockValueFitsCriteriaException {
    history.addValue(SOME_DATE, SOME_VALUE);

    StockValue valueOnDay = history.getValueOnDay(SOME_DATE.plusDays(1));

    assertThat(valueOnDay).isSameAs(SOME_VALUE);
  }

  @Test
  public void givenAPastValueGreaterThanPresentValue_whenGettingTrend_thenReturnDecreasing() {
    history.addValue(SOME_OLDER_DATE, SOME_VALUE);
    history.addValue(SOME_DATE, SOME_LOWER_VALUE);

    StockTrend trend = history.getStockVariationTrendSinceDate(SOME_OLDER_DATE);

    assertThat(trend).isSameAs(DECREASING);
  }

  @Test
  public void givenAPastValueLessThanPresentValue_whenGettingTrend_thenReturnIncreasing() {
    history.addValue(SOME_OLDER_DATE, SOME_VALUE);
    history.addValue(SOME_DATE, SOME_HIGHER_VALUE);

    StockTrend trend = history.getStockVariationTrendSinceDate(SOME_OLDER_DATE);

    assertThat(trend).isSameAs(INCREASING);
  }

  @Test
  public void givenAPastValueEqualToThePresentValue_whenGettingTrend_thenReturnStable() {
    history.addValue(SOME_OLDER_DATE, SOME_VALUE);
    history.addValue(SOME_DATE, SOME_VALUE);

    StockTrend trend = history.getStockVariationTrendSinceDate(SOME_OLDER_DATE);

    assertThat(trend).isSameAs(STABLE);
  }

  @Test
  public void givenAMissingPastValue_whenGettingTrend_thenReturnNoData() {
    history.addValue(SOME_DATE, SOME_VALUE);

    StockTrend trend = history.getStockVariationTrendSinceDate(SOME_OLDER_DATE);

    assertThat(trend).isSameAs(NO_DATA);
  }
}
