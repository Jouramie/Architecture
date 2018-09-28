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
  private final MoneyAmount SOME_NEW_VALUE = new MoneyAmount(120.00, SOME_CURRENCY);

  private StockValue stockValue;

  @Before
  public void setupStockValue() {
    stockValue = new StockValue(SOME_START_VALUE);
  }

  @Test
  public void whenCreated_thenAllValuesAreStartValueAndStockIsClosed() {
    assertThat(stockValue.getOpenValue()).isEqualTo(SOME_START_VALUE);
    assertThat(stockValue.getCurrentValue()).isEqualTo(SOME_START_VALUE);
    assertThat(stockValue.getCloseValue()).isEqualTo(SOME_START_VALUE);
    assertThat(stockValue.isClosed()).isTrue();
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
