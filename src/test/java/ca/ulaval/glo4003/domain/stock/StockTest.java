package ca.ulaval.glo4003.domain.stock;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.util.TestStockBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

public class StockTest {
  private final String SOME_TITLE = "MSFT";
  private final String SOME_CATEGORY = "Banking";
  private final String SOME_NAME = "Microsoft";
  private final MarketId SOME_MARKET_ID = new MarketId("NASDAQ");
  private final double SOME_LAST_OPEN_VALUE = 40.00;
  private final double SOME_START_VALUE = 50.00;
  private final Currency SOME_CURRENCY = new Currency("CAD", new BigDecimal(0.77));
  private final MoneyAmount SOME_LAST_OPEN_AMOUNT = new MoneyAmount(SOME_LAST_OPEN_VALUE, SOME_CURRENCY);
  private final MoneyAmount SOME_START_AMOUNT = new MoneyAmount(SOME_START_VALUE, SOME_CURRENCY);
  private final LocalDate SOME_HISTORICAL_DATE = LocalDate.of(2018, 11, 12);
  private final MoneyAmount SOME_HISTORICAL_AMOUNT = new MoneyAmount(45.67, Currency.USD);

  private Stock stock;

  @Before
  public void setupStock() {
    stock = new TestStockBuilder()
        .withTitle(SOME_TITLE)
        .withName(SOME_NAME)
        .withCategory(SOME_CATEGORY)
        .withMarketId(SOME_MARKET_ID)
        .withHistoricalValue(SOME_HISTORICAL_DATE, new StockValueBuilder().withAllValue(SOME_HISTORICAL_AMOUNT).build())
        .withOpenValue(SOME_LAST_OPEN_AMOUNT)
        .withCloseValue(SOME_START_AMOUNT)
        .build();
  }

  @Test
  public void whenGetTitle_thenReturnTitle() {
    String title = stock.getTitle();

    assertThat(title).isEqualTo(SOME_TITLE);
  }

  @Test
  public void whenGetName_thenReturnName() {
    String name = stock.getName();

    assertThat(name).isEqualTo(SOME_NAME);
  }

  @Test
  public void whenGetCategory_thenReturnCategory() {
    String category = stock.getCategory();

    assertThat(category).isEqualTo(SOME_CATEGORY);
  }

  @Test
  public void whenGetMarketId_thenReturnMarketId() {
    MarketId marketId = stock.getMarketId();

    assertThat(marketId).isEqualTo(SOME_MARKET_ID);
  }

  @Test
  public void whenStockIsCreated_thenStockValueIsFilledWithStartValue() {
    StockValue stockValue = stock.getValue();

    assertThat(stockValue.getLatestValue()).isEqualTo(SOME_START_AMOUNT);
  }

  @Test
  public void whenUpdateValue_thenStockValueIsIncrementedByTheAmount() {
    stock.updateValue(new BigDecimal(10.00));

    assertThat(stock.getValue().getLatestValue()).isEqualTo(new MoneyAmount(SOME_START_VALUE + 10, SOME_CURRENCY));
  }

  @Test
  public void whenSavingOpeningPrice_thenCreateNewStockValueInHistoryWithLatestCloseValue() {
    stock.open();

    assertThat(stock.getValue().getOpenValue()).isEqualTo(SOME_START_AMOUNT);
    assertThat(stock.getValueHistory().getAllStoredValues()).hasSize(3);
  }

  @Test
  public void givenStockIsOpen_whenIsStockClosed_thenFalse() {
    stock.open();

    boolean isStockClosed = stock.isClosed();

    assertThat(isStockClosed).isFalse();
  }

  @Test
  public void givenStockIsOpen_whenSavingClosingPrice_thenCloseStockValue() {
    stock.open();

    stock.close();

    assertThat(stock.isClosed()).isTrue();
  }

  @Test
  public void givenHistoricalDate_whenGetLatestValueOnDate_thenReturnHistoricalValue() {
    StockValue historicalValue = stock.getValueOnDate(SOME_HISTORICAL_DATE).get();

    assertThat(historicalValue.getLatestValue()).isEqualTo(SOME_HISTORICAL_AMOUNT);
  }

  @Test
  public void givenFutureDate_whenGetLatestValueOnDate_thenReturnLatestKnownValue() {
    LocalDate someFutureDate = LocalDate.MAX;

    StockValue latestValue = stock.getValueOnDate(someFutureDate).get();

    assertThat(latestValue.getLatestValue()).isEqualTo(SOME_START_AMOUNT);
  }

  @Test
  public void whenStockVariationIsComputed_thenRelativeVariationIsCalculated()
      throws NoStockValueFitsCriteriaException {
    BigDecimal variation = stock.computeStockValueVariation(SOME_HISTORICAL_DATE);

    assertThat(variation).isEqualTo(SOME_START_AMOUNT.divide(SOME_HISTORICAL_AMOUNT));
  }
}
