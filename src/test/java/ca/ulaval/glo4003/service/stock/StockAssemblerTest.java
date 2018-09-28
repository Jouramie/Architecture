package ca.ulaval.glo4003.service.stock;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;

public class StockAssemblerTest {

  private static final String SOME_TITLE = "someTitle";
  private static final String SOME_NAME = "someName";
  private static final String SOME_MARKET_ID = "NASDAQ";
  private static final double SOME_AMOUNT = 15.3;
  private static final String SOME_CATEGORY = "someCategory";
  private static final String SOME_RATE = "0.77";
  private static final String SOME_RATE_NAME = "CAD";
  private Stock stock;
  private StockAssembler stockAssembler;

  @Before
  public void setUpStock() {
    stockAssembler = new StockAssembler();
  }

  @Test
  public void whenAssemblerToDto_thenStockDtoIsCreated() {
    Stock someStock = buildStock();

    StockDto resultStockDto = stockAssembler.toDto(someStock);

    assertThat(resultStockDto).extracting("title", "name", "category", "market", "open", "current", "close")
        .containsExactly(
            SOME_TITLE,
            SOME_NAME,
            SOME_CATEGORY,
            SOME_MARKET_ID,
            someStock.getValue().getOpenValue().toUsd().doubleValue(),
            someStock.getValue().getCurrentValue().toUsd().doubleValue(),
            someStock.getValue().getCloseValue().toUsd().doubleValue());
  }

  private Stock buildStock() {
    stock = new Stock(
        SOME_TITLE,
        SOME_NAME,
        SOME_CATEGORY,
        new MarketId(SOME_MARKET_ID),
        new MoneyAmount(SOME_AMOUNT, new Currency(SOME_RATE_NAME, new BigDecimal(SOME_RATE))));
    return stock;
  }
}