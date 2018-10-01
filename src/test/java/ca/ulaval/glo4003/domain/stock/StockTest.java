package ca.ulaval.glo4003.domain.stock;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.math.BigDecimal;
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

  private Stock stock;

  @Before
  public void setupStock() {
    stock = new Stock(SOME_TITLE, SOME_NAME, SOME_CATEGORY, SOME_MARKET_ID, SOME_LAST_OPEN_AMOUNT, SOME_START_AMOUNT);
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
  public void whenGetName_thenReturnCategory() {
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

    assertThat(stockValue.getCurrentValue()).isEqualTo(SOME_START_AMOUNT);
  }

  @Test
  public void whenUpdateValue_thenStockValueIsIncrementByTheAmount() {
    stock.updateValue(10.00);

    assertThat(stock.getValue().getCurrentValue()).isEqualTo(new MoneyAmount(60.00, SOME_CURRENCY));
  }

  @Test
  public void whenOpen_thenSetStockValueToCloseValue() {
    stock.open();

    assertThat(stock.getValue().getOpenValue()).isEqualTo(SOME_START_AMOUNT);
  }

  @Test
  public void whenClose_thenCloseStockValue() {
    stock.close();

    assertThat(stock.getValue().isClosed()).isTrue();
  }
}
