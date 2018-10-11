package ca.ulaval.glo4003.ws.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.service.stock.StockService;
import ca.ulaval.glo4003.ws.api.stock.StockDto;
import ca.ulaval.glo4003.ws.api.stock.StockResource;
import ca.ulaval.glo4003.ws.api.stock.StockResourceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockResourceTest {
  private static final String SOME_TITLE = "title";
  private static final String SOME_NAME = "name";
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
  public void whenGetStockByName_thenReturningDto() {
    given(stockService.getStockByName(SOME_NAME)).willReturn(expectedDto);

    StockDto resultingDto = stockResource.getStockByName(SOME_NAME);

    assertThat(resultingDto).isEqualTo(expectedDto);
  }
}
