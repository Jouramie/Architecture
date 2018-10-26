package ca.ulaval.glo4003.service.stock;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.util.TestStockBuilder;
import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class StockAssemblerTest {

  private static final String SOME_TITLE = "someTitle";
  private static final String SOME_NAME = "someName";
  private static final String SOME_MARKET_ID = "NASDAQ";
  private static final String SOME_CATEGORY = "someCategory";
  private static final String SOME_OTHER_TITLE = "someOtherTitle";
  private static final String SOME_OTHER_NAME = "someOtherName";
  private static final String SOME_OTHER_MARKET_ID = "someOtherMarketId";
  private static final String SOME_OTHER_CATEGORY = "someOtherCategory";
  private StockAssembler stockAssembler;

  @Before
  public void setUpStock() {
    stockAssembler = new StockAssembler();
  }

  @Test
  public void whenAssembleToDto_thenStockDtoIsCreated() {
    Stock someStock = new TestStockBuilder()
        .withTitle(SOME_TITLE)
        .withName(SOME_NAME)
        .withCategory(SOME_CATEGORY)
        .withMarketId(new MarketId(SOME_MARKET_ID))
        .build();

    StockDto resultStockDto = stockAssembler.toDto(someStock);

    assertThat(resultStockDto).isEqualToComparingFieldByField(
        new TestStockBuilder()
            .withTitle(SOME_TITLE)
            .withName(SOME_NAME)
            .withCategory(SOME_CATEGORY)
            .withMarketId(new MarketId(SOME_MARKET_ID))
            .buildDto());
  }

  @Test
  public void whenAssembleToDtoList_thenStockDtoListIsCreated() {
    List<Stock> someStockList = Lists.newArrayList(
        new TestStockBuilder()
            .withTitle(SOME_TITLE)
            .withName(SOME_NAME)
            .withCategory(SOME_CATEGORY)
            .withMarketId(new MarketId(SOME_MARKET_ID))
            .build(),
        new TestStockBuilder()
            .withTitle(SOME_OTHER_TITLE)
            .withName(SOME_OTHER_NAME)
            .withCategory(SOME_OTHER_CATEGORY)
            .withMarketId(new MarketId(SOME_OTHER_MARKET_ID))
            .build());

    List<StockDto> resultingStockList = stockAssembler.toDtoList(someStockList);

    assertThat(resultingStockList).usingFieldByFieldElementComparator().containsExactly(
        new TestStockBuilder()
            .withTitle(SOME_TITLE)
            .withName(SOME_NAME)
            .withCategory(SOME_CATEGORY)
            .withMarketId(new MarketId(SOME_MARKET_ID))
            .buildDto(),
        new TestStockBuilder()
            .withTitle(SOME_OTHER_TITLE)
            .withName(SOME_OTHER_NAME)
            .withCategory(SOME_OTHER_CATEGORY)
            .withMarketId(new MarketId(SOME_OTHER_MARKET_ID))
            .buildDto());
  }

  @Test
  public void givenSomeNotUsdStockValue_whenAssembleToDto_thenConvertValueToUsd() {
    Currency someNotUsdCurrency = new Currency("notUsd", BigDecimal.TEN);
    MoneyAmount someNotUsdOpenValue = new MoneyAmount(2, someNotUsdCurrency);
    MoneyAmount someNotUsdCloseValue = new MoneyAmount(3, someNotUsdCurrency);

    Stock someNotUsdStock = new TestStockBuilder()
        .withOpenValue(someNotUsdOpenValue)
        .withCloseValue(someNotUsdCloseValue)
        .build();

    StockDto resultingStockDto = stockAssembler.toDto(someNotUsdStock);

    assertThat(resultingStockDto).isEqualToComparingFieldByField(
        new TestStockBuilder()
            .withOpenValue(someNotUsdOpenValue)
            .withCloseValue(someNotUsdCloseValue)
            .buildDto());
  }
}