package ca.ulaval.glo4003.service.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.cart.CartItem;
import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.util.StockBuilder;
import ca.ulaval.glo4003.ws.api.cart.CartItemResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CartItemAssemblerTest {
  private static final String SOME_TITLE = "someTitle";
  private static final int SOME_QUANTITY = 76;
  private static final String SOME_NAME = "someName";
  private static final String SOME_MARKET_ID = "NASDAQ";
  private static final String SOME_CATEGORY = "someCategory";
  private final Stock SOME_STOCK = new StockBuilder()
      .withTitle(SOME_TITLE)
      .withName(SOME_NAME)
      .withCategory(SOME_CATEGORY)
      .withMarketId(new MarketId(SOME_MARKET_ID))
      .build();

  @Mock
  private StockRepository stockRepository;

  private CartItemAssembler assembler;

  @Before
  public void setupCartItemAssembler() {
    assembler = new CartItemAssembler(stockRepository);

    given(stockRepository.getByTitle(SOME_TITLE)).willReturn(SOME_STOCK);
  }

  @Test
  public void whenToDto_thenFillDtoWithCurrentValue() {
    CartItemResponseDto dto = assembler.toDto(new CartItem(SOME_TITLE, SOME_QUANTITY));

    assertThat(dto.title).isEqualTo(SOME_TITLE);
    assertThat(dto.quantity).isEqualTo(SOME_QUANTITY);
    assertThat(dto.name).isEqualTo(SOME_NAME);
    assertThat(dto.category).isEqualTo(SOME_CATEGORY);
    assertThat(dto.market).isEqualTo(SOME_MARKET_ID);
    assertThat(dto.currentValue).isEqualTo(SOME_STOCK.getValue().getCurrentValue().toUsd());
  }
}
