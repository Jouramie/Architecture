package ca.ulaval.glo4003.service.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.util.TestStockBuilder;
import ca.ulaval.glo4003.ws.api.cart.CartItemResponseDto;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CartItemAssemblerTest {
  private static final String SOME_TITLE = "someTitle";
  private static final String SOME_OTHER_TITLE = "someOtherTitle";
  private static final int SOME_QUANTITY = 76;
  private static final String SOME_NAME = "someName";
  private static final String SOME_MARKET_ID = "NASDAQ";
  private static final String SOME_CATEGORY = "someCategory";
  private final Stock SOME_STOCK = new TestStockBuilder()
      .withTitle(SOME_TITLE)
      .withName(SOME_NAME)
      .withCategory(SOME_CATEGORY)
      .withMarketId(new MarketId(SOME_MARKET_ID))
      .build();
  private final Stock SOME_OTHER_STOCK = new TestStockBuilder()
      .withTitle(SOME_OTHER_TITLE)
      .withName(SOME_NAME)
      .withCategory(SOME_CATEGORY)
      .withMarketId(new MarketId(SOME_MARKET_ID))
      .build();

  @Mock
  private StockRepository stockRepository;

  private CartItemAssembler assembler;

  @Before
  public void setupCartItemAssembler() throws StockNotFoundException {
    assembler = new CartItemAssembler(stockRepository);
    
    given(stockRepository.doesStockExist(SOME_TITLE)).willReturn(true);
    given(stockRepository.doesStockExist(SOME_OTHER_TITLE)).willReturn(true);
    given(stockRepository.findByTitle(SOME_TITLE)).willReturn(SOME_STOCK);
    given(stockRepository.findByTitle(SOME_OTHER_TITLE)).willReturn(SOME_OTHER_STOCK);
  }

  @Test
  public void whenToDto_thenFillDtoWithCurrentValue() {
    CartItemResponseDto dto = assembler.toDto(SOME_TITLE, SOME_QUANTITY);

    assertThatDtoIsCorrectlyMapped(dto);
  }

  @Test
  public void givenInvalidStockTitle_whenToDto_thenInvalidStockTitleExceptionIsThrown()
      throws StockNotFoundException {
    doThrow(StockNotFoundException.class).when(stockRepository).findByTitle(any());

    assertThatThrownBy(() -> assembler.toDto(SOME_TITLE, SOME_QUANTITY))
        .isInstanceOf(InvalidStockTitleException.class);
  }

  @Test
  public void whenToDtoList_thenAllItemsArePresent() {
    StockCollection stockCollection = new StockCollection(stockRepository)
        .add(SOME_TITLE, SOME_QUANTITY)
        .add(SOME_OTHER_TITLE, SOME_QUANTITY);
    List<CartItemResponseDto> dtos = assembler.toDtoList(stockCollection);

    assertThat(dtos).hasSize(2);
  }

  private void assertThatDtoIsCorrectlyMapped(CartItemResponseDto dto) {
    assertThat(dto.title).isEqualTo(SOME_TITLE);
    assertThat(dto.quantity).isEqualTo(SOME_QUANTITY);
    assertThat(dto.name).isEqualTo(SOME_NAME);
    assertThat(dto.category).isEqualTo(SOME_CATEGORY);
    assertThat(dto.market).isEqualTo(SOME_MARKET_ID);
    assertThat(dto.currentValue).isEqualTo(SOME_STOCK.getValue().getCurrentValue().toUsd());
  }
}
