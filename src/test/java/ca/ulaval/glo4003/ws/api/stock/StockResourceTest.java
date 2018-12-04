package ca.ulaval.glo4003.ws.api.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.service.stock.StockDto;
import ca.ulaval.glo4003.service.stock.StockService;
import ca.ulaval.glo4003.ws.api.stock.assemblers.ApiStockAssembler;
import ca.ulaval.glo4003.ws.api.stock.dtos.ApiStockDto;
import ca.ulaval.glo4003.ws.api.stock.resources.StockResourceImpl;
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
  private static final String SOME_CATEGORY = "technology";
  private static final int DEFAULT_PAGE = 1;
  private static final int DEFAULT_PER_PAGE = 15;

  @Mock
  private StockService stockService;
  @Mock
  private ApiStockAssembler apiStockAssembler;
  @Mock
  private StockDto serviceDto;
  @Mock
  private ApiStockDto expectedDto;
  @Mock
  private List<ApiStockDto> expectedDtos;
  @Mock
  private List<StockDto> serviceDtos;

  private StockResourceImpl stockResource;

  @Before
  public void setupStockResource() {
    stockResource = new StockResourceImpl(stockService, apiStockAssembler);
  }

  @Test
  public void whenGetStockByTitle_thenReturningDto() {
    given(stockService.getStockByTitle(SOME_TITLE)).willReturn(serviceDto);
    given(apiStockAssembler.toDto(serviceDto)).willReturn(expectedDto);

    ApiStockDto resultingDto = stockResource.getStockByTitle(SOME_TITLE);

    assertThat(resultingDto).isSameAs(expectedDto);
  }

  @Test
  public void whenGetStocks_thenQueryStocks() {
    stockResource.getStocks(SOME_NAME, SOME_CATEGORY, DEFAULT_PAGE, DEFAULT_PER_PAGE);

    verify(stockService).queryStocks(SOME_NAME, SOME_CATEGORY);
  }

  @Test
  public void whenGetStocks_thenReturnStocks() {
    given(stockService.queryStocks(any(), any())).willReturn(serviceDtos);
    given(apiStockAssembler.toDtoList(serviceDtos)).willReturn(expectedDtos);

    List<ApiStockDto> resultingDtos = stockResource.getStocks(SOME_NAME, SOME_CATEGORY, DEFAULT_PAGE,
        DEFAULT_PER_PAGE);

    assertThat(resultingDtos).isSameAs(expectedDtos);
  }
}
