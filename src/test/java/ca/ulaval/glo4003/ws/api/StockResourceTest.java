package ca.ulaval.glo4003.ws.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.service.stock.StockDto;
import ca.ulaval.glo4003.service.stock.StockService;
import ca.ulaval.glo4003.ws.api.stock.StockResource;
import ca.ulaval.glo4003.ws.api.stock.StockResourceImpl;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockResourceTest {
  private static final String SOME_TITLE = "title";
  private static final String SOME_NAME = "name";
  private static final int DEFAULT_PAGE = 1;
  private static final int DEFAULT_PER_PAGE = 15;
  @Mock
  private StockService stockService;
  @Mock
  private StockDto expectedDto;
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
}
