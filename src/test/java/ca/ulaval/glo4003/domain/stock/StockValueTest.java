package ca.ulaval.glo4003.domain.stock;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.math.BigDecimal;
import org.junit.Test;

public class StockValueTest {
  private final Currency SOME_CURRENCY = new Currency("CAD", new BigDecimal(0.77));
  private final MoneyAmount SOME_START_VALUE = new MoneyAmount(100.00, SOME_CURRENCY);
  private final MoneyAmount SOME_CLOSE_VALUE = new MoneyAmount(110.00, SOME_CURRENCY);
  private final MoneyAmount SOME_NEW_VALUE = new MoneyAmount(120.00, SOME_CURRENCY);
  private final MoneyAmount SOME_BIGGER_VALUE = new MoneyAmount(300.00, SOME_CURRENCY);
  private final MoneyAmount SOME_SMALLER_VALUE = new MoneyAmount(30.00, SOME_CURRENCY);

  @Test
  public void whenCreateOpen_thenAllValuesAreSetToStartValueAndStockIsOpen() {
    StockValue stockValue = StockValue.createOpen(SOME_START_VALUE);

    assertThat(stockValue.getOpenValue()).isEqualTo(SOME_START_VALUE);
    assertThat(stockValue.getLatestValue()).isEqualTo(SOME_START_VALUE);
    assertThat(stockValue.getMaximumValue()).isEqualTo(SOME_START_VALUE);
    assertThat(stockValue.isClosed()).isFalse();
  }

  @Test
  public void whenCreateClosedWithExplicitValues_thenValuesAreSetAppropriatelyAndStockIsClosed() {
    StockValue stockValue = StockValue.createClosed(SOME_START_VALUE, SOME_CLOSE_VALUE, SOME_BIGGER_VALUE);

    assertThat(stockValue.getOpenValue()).isEqualTo(SOME_START_VALUE);
    assertThat(stockValue.getLatestValue()).isEqualTo(SOME_CLOSE_VALUE);
    assertThat(stockValue.getMaximumValue()).isEqualTo(SOME_BIGGER_VALUE);
    assertThat(stockValue.isClosed()).isTrue();
  }

  @Test
  public void givenClosedStockValue_whenSetValue_thenOpenStockAndSetOpenAndLatestValue() {
    StockValue stockValue = StockValue.createClosed(SOME_START_VALUE, SOME_START_VALUE, SOME_START_VALUE);

    StockValue newStockValue = stockValue.setValue(SOME_NEW_VALUE);

    assertThat(newStockValue.getOpenValue()).isEqualTo(SOME_NEW_VALUE);
    assertThat(newStockValue.getLatestValue()).isEqualTo(SOME_NEW_VALUE);
    assertThat(newStockValue.isClosed()).isFalse();
  }

  @Test
  public void givenOpenStockValue_whenSetValue_thenSetLatestValueOnly() {
    StockValue stockValue = StockValue.createOpen(SOME_START_VALUE);

    StockValue newStockValue = stockValue.setValue(SOME_NEW_VALUE);

    assertThat(newStockValue.getOpenValue()).isEqualTo(SOME_START_VALUE);
    assertThat(newStockValue.getLatestValue()).isEqualTo(SOME_NEW_VALUE);
  }

  @Test
  public void whenUpdateValue_thenStockValueIsIncrementedByTheAmount() {
    StockValue stockValue = StockValue.createOpen(SOME_START_VALUE);

    StockValue newStockValue = stockValue.updateValue(new BigDecimal(10.00));

    assertThat(newStockValue.getLatestValue()).isEqualTo(SOME_START_VALUE.add(BigDecimal.valueOf(10)));
  }

  @Test
  public void givenOpenStockValue_whenSetValueWithBiggerValue_thenSetUpdateMaximumValue() {
    StockValue stockValue = StockValue.createOpen(SOME_START_VALUE);

    StockValue newStockValue = stockValue.setValue(SOME_BIGGER_VALUE);

    assertThat(newStockValue.getMaximumValue()).isEqualTo(SOME_BIGGER_VALUE);
  }

  @Test
  public void givenOpenStockValue_whenSetValueWithSmallerValue_thenDoNotUpdateMaximumValue() {
    StockValue stockValue = StockValue.createOpen(SOME_START_VALUE);

    StockValue newStockValue = stockValue.setValue(SOME_SMALLER_VALUE);

    assertThat(newStockValue.getMaximumValue()).isEqualTo(SOME_START_VALUE);
  }

  @Test
  public void givenOpenStockValue_whenClose_thenSetCloseAttribute() {
    StockValue stockValue = StockValue.createOpen(SOME_START_VALUE);

    StockValue newStockValue = stockValue.close();

    assertThat(newStockValue.isClosed()).isTrue();
  }

  // TODO ajouter tests pour vérifier l'immuabilité
}
