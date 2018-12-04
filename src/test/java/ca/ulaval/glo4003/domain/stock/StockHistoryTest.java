package ca.ulaval.glo4003.domain.stock;

import static ca.ulaval.glo4003.domain.stock.StockTrend.DECREASING;
import static ca.ulaval.glo4003.domain.stock.StockTrend.INCREASING;
import static ca.ulaval.glo4003.domain.stock.StockTrend.NO_DATA;
import static ca.ulaval.glo4003.domain.stock.StockTrend.STABLE;
import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class StockHistoryTest {
  private static final LocalDate SOME_DATE = LocalDate.now();
  private static final LocalDate SOME_OLDER_DATE = SOME_DATE.minusDays(1);
  private static final LocalDate SOME_MORE_RECENT_DATE = SOME_DATE.plusDays(1);
  private static final StockValue SOME_VALUE = new StockValueBuilder().withAllValue(12.34).build();
  private static final StockValue SOME_OTHER_VALUE = new StockValueBuilder().withAllValue(45.67).build();
  private static final StockValue SOME_LOWER_VALUE = new StockValueBuilder().withAllValue(1).build();
  private static final StockValue SOME_HIGHER_VALUE = new StockValueBuilder().withAllValue(300.00).build();
  private static final StockValue SOME_EVEN_HIGHER_VALUE = new StockValueBuilder().withAllValue(500.00).build();
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

    HistoricalStockValue result = history.getLatestHistoricalValue();

    assertThat(result.date).isEqualTo(SOME_MORE_RECENT_DATE);
    assertThat(result.value).isEqualTo(SOME_OTHER_VALUE);
  }

  @Test
  public void givenHistoryWithSingleValue_whenAddNextValue_thenAddValueWithDateSetToNextDay() {
    history.addValue(SOME_DATE, SOME_VALUE);

    history.addNextValue(SOME_OTHER_VALUE);

    HistoricalStockValue result = history.getLatestHistoricalValue();
    assertThat(result.date).isEqualTo(SOME_DATE.plusDays(1));
    assertThat(result.value).isEqualTo(SOME_OTHER_VALUE);
  }

  @Test
  public void givenHistoryWithMultipleValues_whenGetMaxValue_thenReturnMaxValueInInterval() {
    history.addValue(START_DATE.minusDays(1), SOME_EVEN_HIGHER_VALUE);
    history.addValue(START_DATE, SOME_VALUE);
    history.addValue(MIDDLE_DATE, SOME_HIGHER_VALUE);
    history.addValue(END_DATE, SOME_VALUE);
    history.addValue(END_DATE.plusDays(1), SOME_EVEN_HIGHER_VALUE);

    HistoricalStockValue result = history.getMaxValue(START_DATE, END_DATE).get();

    assertThat(result.date).isSameAs(MIDDLE_DATE);
    assertThat(result.value).isSameAs(SOME_HIGHER_VALUE);
  }

  @Test
  public void givenHistoryWithMaxValueOnStartDate_whenGetMaxValue_thenReturnMaxValueInInterval() {
    history.addValue(START_DATE.minusDays(1), SOME_EVEN_HIGHER_VALUE);
    history.addValue(START_DATE, SOME_HIGHER_VALUE);
    history.addValue(MIDDLE_DATE, SOME_VALUE);
    history.addValue(END_DATE, SOME_VALUE);
    history.addValue(END_DATE.plusDays(1), SOME_EVEN_HIGHER_VALUE);

    HistoricalStockValue result = history.getMaxValue(START_DATE, END_DATE).get();

    assertThat(result.date).isSameAs(START_DATE);
    assertThat(result.value).isSameAs(SOME_HIGHER_VALUE);
  }

  @Test
  public void givenHistoryWithMaxValueOnEndDate_whenGetMaxValue_thenReturnMaxValueInInterval() {
    history.addValue(START_DATE.minusDays(1), SOME_EVEN_HIGHER_VALUE);
    history.addValue(START_DATE, SOME_VALUE);
    history.addValue(MIDDLE_DATE, SOME_VALUE);
    history.addValue(END_DATE, SOME_HIGHER_VALUE);
    history.addValue(END_DATE.plusDays(1), SOME_EVEN_HIGHER_VALUE);

    HistoricalStockValue result = history.getMaxValue(START_DATE, END_DATE).get();

    assertThat(result.date).isSameAs(END_DATE);
    assertThat(result.value).isSameAs(SOME_HIGHER_VALUE);
  }

  @Test
  public void givenEmptyHistory_whenGetMaxValue_thenExceptionIsThrow() {
    Optional<HistoricalStockValue> optionalStockValue = history.getMaxValue(START_DATE, END_DATE);

    assertThat(optionalStockValue).isEmpty();
  }

  @Test
  public void givenNoData_whenGettingValueOnASpecificDay_thenEmptyOptionalIsReturned() {
    LocalDate missingDate = LocalDate.of(1970, Month.JANUARY, 1);

    Optional<StockValue> optionalStockValue = history.getValueOnDay(missingDate);

    assertThat(optionalStockValue).isEmpty();
  }

  @Test
  public void whenGettingValueOnASpecificDay_thenReturnStockValueOnThatDay() {
    history.addValue(SOME_DATE, SOME_VALUE);

    StockValue valueOnDay = history.getValueOnDay(SOME_DATE).get();

    assertThat(valueOnDay).isSameAs(SOME_VALUE);
  }

  @Test
  public void givenNoDataOnRequestedDay_whenGettingValueOnASpecificDay_thenReturnPreviousValue() {
    history.addValue(SOME_DATE, SOME_VALUE);

    StockValue valueOnDay = history.getValueOnDay(SOME_DATE.plusDays(1)).get();

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

  @Test
  public void givenVariation_whenUpdateCurrentValue_thenAddVariationToCurrentValue() {
    history.addValue(SOME_DATE, new StockValueBuilder().withLatestValue(10).build());

    history.updateCurrentValue(BigDecimal.ONE);

    assertThat(history.getLatestValue().getLatestValue()).isEqualTo(new MoneyAmount(11));
  }
}
