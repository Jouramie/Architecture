package ca.ulaval.glo4003.domain.stock;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.math.BigDecimal;
import org.junit.Test;

public class StockValueTest {
  private static final BigDecimal SOME_VARIATION = BigDecimal.TEN;

  @Test
  public void whenUpdateCurrentValue_thenDoNotChangeOpenValue() {
    StockValue stockValue = new StockValueBuilder().build();

    StockValue newStockValue = stockValue.updateCurrentValue(SOME_VARIATION);

    assertThat(newStockValue.getOpenValue()).isSameAs(StockValueBuilder.DEFAULT_OPEN_VALUE);
  }

  @Test
  public void givenVariation_whenUpdateCurrentValue_thenStockValueIsIncrementedByTheAmount() {
    MoneyAmount latestValue = new MoneyAmount(10);
    BigDecimal variation = BigDecimal.ONE;
    StockValue stockValue = new StockValueBuilder().withLatestValue(latestValue).build();

    StockValue newStockValue = stockValue.updateCurrentValue(variation);

    MoneyAmount expectedLatestValue = latestValue.add(variation);
    assertThat(newStockValue.getLatestValue()).isEqualTo(expectedLatestValue);
  }

  @Test
  public void givenVariationExceedingMaximumValue_whenUpdateCurrentValue_thenUpdateMaximumValue() {
    MoneyAmount latestValue = new MoneyAmount(10);
    BigDecimal variation = BigDecimal.TEN;
    MoneyAmount maximumValue = latestValue.add(BigDecimal.ONE);
    StockValue stockValue = new StockValueBuilder().withLatestValue(latestValue).withMaximumValue(maximumValue).build();

    StockValue newStockValue = stockValue.updateCurrentValue(variation);

    MoneyAmount expectedMaximumValue = latestValue.add(variation);
    assertThat(newStockValue.getMaximumValue()).isEqualTo(expectedMaximumValue);
  }

  @Test
  public void givenVariationNotExceedingMaximumValue_whenUpdateCurrentValue_thenDoNotUpdateMaximumValue() {
    MoneyAmount latestValue = new MoneyAmount(10);
    BigDecimal variation = BigDecimal.ONE;
    MoneyAmount maximumValue = latestValue.add(BigDecimal.TEN);
    StockValue stockValue = new StockValueBuilder().withLatestValue(latestValue).withMaximumValue(maximumValue).build();

    StockValue newStockValue = stockValue.updateCurrentValue(variation);

    assertThat(newStockValue.getMaximumValue()).isSameAs(maximumValue);
  }

  @Test
  public void whenUpdateCurrentValue_thenObjectIsNotModified() {
    StockValue stockValue = new StockValueBuilder().build();

    stockValue.updateCurrentValue(SOME_VARIATION);

    assertThat(stockValue.getLatestValue()).isSameAs(StockValueBuilder.DEFAULT_LATEST_VALUE);
    assertThat(stockValue.getMaximumValue()).isSameAs(StockValueBuilder.DEFAULT_MAXIMUM_VALUE);
    assertThat(stockValue.getOpenValue()).isSameAs(StockValueBuilder.DEFAULT_OPEN_VALUE);
  }
}
