package ca.ulaval.glo4003.domain.stock;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;

public class StockValueTest {
  private final Currency SOME_CURRENCY = new Currency("CAD", new BigDecimal(0.77));
  private final MoneyAmount SOME_START_VALUE = new MoneyAmount(100.00, SOME_CURRENCY);
  private final MoneyAmount SOME_CLOSE_VALUE = new MoneyAmount(110.00, SOME_CURRENCY);
  private final MoneyAmount SOME_NEW_VALUE = new MoneyAmount(120.00, SOME_CURRENCY);
  private final MoneyAmount SOME_BIGGER_VALUE = new MoneyAmount(300.00, SOME_CURRENCY);
  private final MoneyAmount SOME_SMALLER_VALUE = new MoneyAmount(30.00, SOME_CURRENCY);

  private StockValue stockValue;

  @Before
  public void setupStockValue() {
    stockValue = new StockValue(SOME_START_VALUE);
  }

  @Test
  public void whenCreated_thenAllValuesAreSetToStartValueAndStockIsOpen() {
    assertThat(stockValue.getOpenValue()).isEqualTo(SOME_START_VALUE);
    assertThat(stockValue.getCurrentValue()).isEqualTo(SOME_START_VALUE);
    assertThat(stockValue.getCloseValue()).isNull();
    assertThat(stockValue.getMaximumValue()).isEqualTo(SOME_START_VALUE);
    assertThat(stockValue.isClosed()).isFalse();
  }

  @Test
  public void whenCreatedWithExplicitValues_thenCurrentAndCloseValuesAreSetToCloseValue() {
    StockValue newStockValue = new StockValue(SOME_START_VALUE, SOME_CLOSE_VALUE, SOME_BIGGER_VALUE);

    assertThat(newStockValue.getOpenValue()).isEqualTo(SOME_START_VALUE);
    assertThat(newStockValue.getCurrentValue()).isEqualTo(SOME_CLOSE_VALUE);
    assertThat(newStockValue.getCloseValue()).isEqualTo(SOME_CLOSE_VALUE);
    assertThat(newStockValue.getMaximumValue()).isEqualTo(SOME_BIGGER_VALUE);
    assertThat(newStockValue.isClosed()).isTrue();
  }

  @Test
  public void givenClosedStockValue_whenSetValue_thenSetOpenAndCurrentValueAndSetCloseValueToNull() {
    stockValue.close();

    stockValue.setValue(SOME_NEW_VALUE);

    assertThat(stockValue.getOpenValue()).isEqualTo(SOME_NEW_VALUE);
    assertThat(stockValue.getCurrentValue()).isEqualTo(SOME_NEW_VALUE);
    assertThat(stockValue.getCloseValue()).isNull();
    assertThat(stockValue.isClosed()).isFalse();
  }

  @Test
  public void givenOpenStockValue_whenSetValue_thenSetCurrentValueOnly() {
    stockValue.setValue(SOME_START_VALUE);

    stockValue.setValue(SOME_NEW_VALUE);

    assertThat(stockValue.getOpenValue()).isEqualTo(SOME_START_VALUE);
    assertThat(stockValue.getCurrentValue()).isEqualTo(SOME_NEW_VALUE);
    assertThat(stockValue.getCloseValue()).isNull();
    assertThat(stockValue.isClosed()).isFalse();
  }

  @Test
  public void givenOpenStockValue_whenSetValueWithBiggerValue_thenSetUpdateMaximumValue() {
    stockValue.setValue(SOME_START_VALUE);

    stockValue.setValue(SOME_BIGGER_VALUE);

    assertThat(stockValue.getMaximumValue()).isEqualTo(SOME_BIGGER_VALUE);
  }

  @Test
  public void givenOpenStockValue_whenSetValueWithSmallerValue_thenDontUpdateMaximumValue() {
    stockValue.setValue(SOME_START_VALUE);

    stockValue.setValue(SOME_SMALLER_VALUE);

    assertThat(stockValue.getMaximumValue()).isEqualTo(SOME_START_VALUE);
  }

  @Test
  public void givenOpenStockValue_whenClose_thenSetCloseValueToCurrentValue() {
    stockValue.setValue(SOME_START_VALUE);
    stockValue.setValue(SOME_NEW_VALUE);

    stockValue.close();

    assertThat(stockValue.getOpenValue()).isEqualTo(SOME_START_VALUE);
    assertThat(stockValue.getCurrentValue()).isEqualTo(SOME_NEW_VALUE);
    assertThat(stockValue.getCloseValue()).isEqualTo(SOME_NEW_VALUE);
    assertThat(stockValue.isClosed()).isTrue();
  }
}
