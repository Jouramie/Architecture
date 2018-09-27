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

  private Stock stock;
  private StockAssembler stockAssembler;

  @Before
  public void setUpStock() {
    stockAssembler = new StockAssembler();
  }

  @Test
  public void whenCreateStockDto_thenReturnStockDto() {
    Stock someStock = buildStock();

    StockDto resultStockDto = stockAssembler.toDto(someStock);

    assertThat(resultStockDto).extracting("title", "name", "category", "market", "open", "current", "close")
        .containsExactly(
            "someTitle",
            "someName",
            "someCategory",
            "NASDAQ",
            someStock.getValue().getOpenValue().toUsd().doubleValue(),
            someStock.getValue().getCurrentValue().toUsd().doubleValue(),
            someStock.getValue().getCloseValue().toUsd().doubleValue());
  }

  public Stock buildStock() {
    stock = new Stock(
        "someTitle",
        "someName",
        "someCategory",
        new MarketId("NASDAQ"),
        new MoneyAmount(15.3, new Currency("CAD", new BigDecimal("0.77"))));
    return stock;
  }
}