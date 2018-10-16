package ca.ulaval.glo4003.ws.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.service.stock.StockService;
import ca.ulaval.glo4003.service.stock.max.StockMaxValueSinceRange;
import ca.ulaval.glo4003.ws.api.stock.StockDto;
import ca.ulaval.glo4003.ws.api.stock.StockResource;
import ca.ulaval.glo4003.ws.api.stock.StockResourceImpl;
import ca.ulaval.glo4003.ws.api.stock.max.StockMaxResponseDto;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.BadRequestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockResourceTest {
  private static final String SOME_TITLE = "title";
  private static final String SOME_NAME = "name";
  private static final StockMaxValueSinceRange SOME_RANGE = StockMaxValueSinceRange.LAST_FIVE_DAYS;
  private static final int DEFAULT_PAGE = 1;
  private static final int DEFAULT_PER_PAGE = 15;
  @Mock
  private StockService stockService;
  @Mock
  private StockDto expectedDto;
  @Mock
  private StockMaxResponseDto expectedMaxResponseDto;

  private StockResource stockResource;

  @Before
  public void setup() {
    stockResource = new StockResourceImpl(stockService);
  }

  @Test
  public void whenGetStockByTitle_thenReturningDto() {
    given(stockService.getStockByTitle(SOME_TITLE)).willReturn(expectedDto);

    StockDto resultingDto = stockResource.getStockByTitle(SOME_TITLE);

    assertThat(resultingDto).isEqualTo(expectedDto);
  }

  @Test
  public void whenGetStock_thenReturningSingletonListOfDto() {
    given(stockService.getStockByName(SOME_NAME)).willReturn(expectedDto);

    List<StockDto> resultingDto = stockResource.getStocks(SOME_NAME, null, DEFAULT_PAGE, DEFAULT_PER_PAGE);

    assertThat(resultingDto).isEqualTo(Collections.singletonList(expectedDto));
  }

  @Test
  public void whenGetStockMaxValue_thenReturningDto() {
    given(stockService.getStockMaxValue(SOME_TITLE, SOME_RANGE)).willReturn(expectedMaxResponseDto);

    StockMaxResponseDto resultingDto = stockResource.getStockMaxValue(SOME_TITLE, SOME_RANGE.toString());

    assertThat(resultingDto).isEqualTo(expectedMaxResponseDto);
  }

  @Test
  public void givenInvalidSinceParameter_whenGetStockMax_thenThrowBadRequest() {
    assertThatThrownBy(() -> {
      stockResource.getStockMaxValue(SOME_TITLE, "wrong");
    }).isInstanceOf(BadRequestException.class);
  }
}
