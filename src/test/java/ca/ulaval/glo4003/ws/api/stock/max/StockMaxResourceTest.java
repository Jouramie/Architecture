package ca.ulaval.glo4003.ws.api.stock.max;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.service.stock.max.StockMaxValueService;
import ca.ulaval.glo4003.service.stock.max.dto.StockMaxValueSummary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockMaxResourceTest {
  private static final String SOME_TITLE = "title";

  @Mock
  private StockMaxValueService stockMaxValueService;
  @Mock
  private StockMaxResponseDtoAssembler stockMaxResponseDtoAssembler;
  @Mock
  private StockMaxValueSummary expectedSummary;
  @Mock
  private ApiStockMaxResponseDto expectedMaxResponseDto;
  @Mock
  private StockMaxResponseDto serviceMaxResponseDto;
  @Mock
  private ApiStockMaxResponseAssembler apiStockMaxResponseAssembler;

  private StockMaxResourceImpl stockMaxResource;

  @Before
  public void setupStockMaxResourceImpl() {
    stockMaxResource = new StockMaxResourceImpl(stockMaxValueService, stockMaxResponseDtoAssembler);
  }

  @Test
  public void whenGetStockMaxValue_thenReturningDto() {
    given(stockMaxValueService.getStockMaxValue(SOME_TITLE)).willReturn(expectedSummary);
    given(apiStockMaxResponseDtoAssembler.toDto(SOME_TITLE, expectedSummary)).willReturn(expectedMaxResponseDto);

    ApiStockMaxResponseDto resultingDto = stockMaxResource.getStockMaxValue(SOME_TITLE);

    assertThat(resultingDto).isEqualTo(expectedMaxResponseDto);
  }
}
