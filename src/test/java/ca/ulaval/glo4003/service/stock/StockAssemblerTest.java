package ca.ulaval.glo4003.service.stock;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.util.StockBuilder;
import org.junit.Before;
import org.junit.Test;

public class StockAssemblerTest {

  private static final String SOME_TITLE = "someTitle";
  private static final String SOME_NAME = "someName";
  private static final String SOME_MARKET_ID = "NASDAQ";
  private static final String SOME_CATEGORY = "someCategory";
  private StockAssembler stockAssembler;

  @Before
  public void setUpStock() {
    stockAssembler = new StockAssembler();
  }

  @Test
  public void whenAssemblerToDto_thenStockDtoIsCreated() {
    Stock someStock = new StockBuilder()
        .withTitle(SOME_TITLE)
        .withName(SOME_NAME)
        .withCategory(SOME_CATEGORY)
        .withMarketId(new MarketId(SOME_MARKET_ID))
        .build();

    StockDto resultStockDto = stockAssembler.toDto(someStock);

    assertThat(resultStockDto).extracting("title", "name", "category", "market", "openValue", "currentValue", "closeValue")
        .containsExactly(
            SOME_TITLE,
            SOME_NAME,
            SOME_CATEGORY,
            SOME_MARKET_ID,
            someStock.getValue().getOpenValue().toUsd(),
            someStock.getValue().getCurrentValue().toUsd(),
            someStock.getValue().getCloseValue().toUsd());
  }
}