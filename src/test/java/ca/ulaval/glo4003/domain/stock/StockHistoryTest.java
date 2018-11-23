package ca.ulaval.glo4003.domain.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.time.LocalDate;
import java.time.Month;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;

public class StockHistoryTest {
  private final Currency SOME_CURRENCY = Currency.USD;
  private final double SOME_AMOUNT = 12.34;
  private final double SOME_OTHER_AMOUNT = 45.67;
  private final double SOME_BIGGER_AMOUNT = 300.00;
  private final double SOME_EVEN_BIGGER_AMOUNT = 500.00;
  private final LocalDate SOME_DATE = LocalDate.now();
  private final LocalDate SOME_MORE_RECENT_DATE = LocalDate.now().plusDays(1);
  private final LocalDate START_DATE = LocalDate.of(2018, 9, 12);
  private final LocalDate END_DATE = LocalDate.of(2018, 10, 12);
  private final LocalDate MIDDLE_DATE = LocalDate.of(2018, 10, 1);

  private StockHistory history;

  @Before
  public void setup() {
    history = new StockHistory();
  }

  @Test
  public void whenAddValue_thenAddValueToTheHistory() {
    StockValue value = buildStockValue(SOME_AMOUNT);

    history.addValue(SOME_DATE, value);

    assertThat(history.getAllStoredValues()).hasSize(1);
    assertThat(history.getAllStoredValues()).first().extracting("value", "date")
        .contains(value, SOME_DATE);
  }

  @Test
  public void givenHistoryWithTwoValues_whenGetLatestValue_thenReturnMostRecentValue() {
    StockValue mostRecentValue = buildStockValue(SOME_AMOUNT);
    StockValue oldValue = buildStockValue(SOME_OTHER_AMOUNT);
    history.addValue(SOME_MORE_RECENT_DATE, mostRecentValue);
    history.addValue(SOME_DATE, oldValue);

    HistoricalStockValue result = history.getLatestValue();

    assertThat(result.date).isEqualTo(SOME_MORE_RECENT_DATE);
    assertThat(result.value).isEqualTo(mostRecentValue);
  }

  @Test
  public void givenHistoryWithSingleValue_whenAddNextValue_thenAddValueWithDateSetToNextDay() {
    StockValue oldValue = buildStockValue(SOME_AMOUNT);
    history.addValue(SOME_DATE, oldValue);

    StockValue newValue = buildStockValue(SOME_OTHER_AMOUNT);
    history.addNextValue(newValue);

    HistoricalStockValue result = history.getLatestValue();
    assertThat(result.date).isEqualTo(SOME_DATE.plusDays(1));
    assertThat(result.value).isEqualTo(newValue);
  }

  @Test
  public void givenHistoryWithMultipleValues_whenGetMaxValue_thenReturnMaxValueInInterval() throws NoStockValueFitsCriteriaException {
    StockValue maxValue = buildStockValue(SOME_BIGGER_AMOUNT);
    history.addValue(START_DATE.minusDays(1), buildStockValue(SOME_EVEN_BIGGER_AMOUNT));
    history.addValue(START_DATE, buildStockValue(SOME_AMOUNT));
    history.addValue(MIDDLE_DATE, maxValue);
    history.addValue(END_DATE, buildStockValue(SOME_AMOUNT));
    history.addValue(END_DATE.plusDays(1), buildStockValue(SOME_EVEN_BIGGER_AMOUNT));

    HistoricalStockValue result = history.getMaxValue(START_DATE, END_DATE);

    assertThat(result.date).isEqualTo(MIDDLE_DATE);
    assertThat(result.value).isEqualTo(maxValue);
  }

  @Test
  public void givenHistoryWithMaxValueOnStartDate_whenGetMaxValue_thenReturnMaxValueInInterval() throws NoStockValueFitsCriteriaException {
    StockValue maxValue = buildStockValue(SOME_BIGGER_AMOUNT);
    history.addValue(START_DATE.minusDays(1), buildStockValue(SOME_EVEN_BIGGER_AMOUNT));
    history.addValue(START_DATE, maxValue);
    history.addValue(MIDDLE_DATE, buildStockValue(SOME_AMOUNT));
    history.addValue(END_DATE, buildStockValue(SOME_AMOUNT));
    history.addValue(END_DATE.plusDays(1), buildStockValue(SOME_EVEN_BIGGER_AMOUNT));

    HistoricalStockValue result = history.getMaxValue(START_DATE, END_DATE);

    assertThat(result.date).isEqualTo(START_DATE);
    assertThat(result.value).isEqualTo(maxValue);
  }

  @Test
  public void givenHistoryWithMaxValueOnEndDate_whenGetMaxValue_thenReturnMaxValueInInterval() throws NoStockValueFitsCriteriaException {
    StockValue maxValue = buildStockValue(SOME_BIGGER_AMOUNT);
    history.addValue(START_DATE.minusDays(1), buildStockValue(SOME_EVEN_BIGGER_AMOUNT));
    history.addValue(START_DATE, buildStockValue(SOME_AMOUNT));
    history.addValue(MIDDLE_DATE, buildStockValue(SOME_AMOUNT));
    history.addValue(END_DATE, maxValue);
    history.addValue(END_DATE.plusDays(1), buildStockValue(SOME_EVEN_BIGGER_AMOUNT));

    HistoricalStockValue result = history.getMaxValue(START_DATE, END_DATE);

    assertThat(result.date).isEqualTo(END_DATE);
    assertThat(result.value).isEqualTo(maxValue);
  }

  @Test
  public void givenEmptyHistory_whenGetMaxValue_thenExceptionIsThrow() {
    ThrowingCallable getMaxValue = () -> history.getMaxValue(START_DATE, END_DATE);

    assertThatThrownBy(getMaxValue).isInstanceOf(NoStockValueFitsCriteriaException.class);
  }

  private StockValue buildStockValue(double value) {
    MoneyAmount amount = new MoneyAmount(value, SOME_CURRENCY);
    return new StockValue(amount);
  }

  @Test
  public void givenNoData_whenGettingValueOnASpecificDay_thenExceptionIsThrow() {
    LocalDate missingDate = LocalDate.of(1970, Month.JANUARY, 1);

    ThrowingCallable getValue = () -> history.getValueOnDay(missingDate);

    assertThatThrownBy(getValue);
  }

  @Test
  public void givenNoDataOnRequestedDay_whenGettingValueOnASpecificDay_thenReturnPreviousValue()
      throws NoStockValueFitsCriteriaException {
    history.addValue(SOME_DATE, buildStockValue(SOME_AMOUNT));

    StockValue valueOnDay = history.getValueOnDay(SOME_DATE.plusDays(1));
    assertThat(valueOnDay.getLatestValue().getAmount().doubleValue()).isEqualTo(SOME_AMOUNT);
  }

  @Test
  public void whenGettingValueOnASpecificDay_thenReturnStockValueOnThatDay()
      throws NoStockValueFitsCriteriaException {
    history.addValue(SOME_DATE, buildStockValue(SOME_AMOUNT));

    StockValue valueOnDay = history.getValueOnDay(SOME_DATE);
    assertThat(valueOnDay.getLatestValue().getAmount().doubleValue()).isEqualTo(SOME_AMOUNT);
  }
}
