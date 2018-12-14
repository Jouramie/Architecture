package ca.ulaval.glo4003.ws.api.stock.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.service.stock.max.StockMaxValueService;
import ca.ulaval.glo4003.service.stock.max.StockMaxValueSummary;
import ca.ulaval.glo4003.ws.api.stock.assembler.StockMaxResponseDtoAssembler;
import ca.ulaval.glo4003.ws.api.stock.dto.StockMaxResponseDto;
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
  private StockMaxResponseDto expectedMaxResponseDto;

  private StockMaxResource stockMaxResource;

  @Before
  public void setupStockMaxResource() {
    stockMaxResource = new StockMaxResource(stockMaxValueService, stockMaxResponseDtoAssembler);
  }

  @Test
  public void whenGetStockMaxValue_thenReturningDto() {
    given(stockMaxValueService.getStockMaxValue(SOME_TITLE)).willReturn(expectedSummary);
    given(stockMaxResponseDtoAssembler.toDto(SOME_TITLE, expectedSummary)).willReturn(expectedMaxResponseDto);

    StockMaxResponseDto resultingDto = stockMaxResource.getStockMaxValue(SOME_TITLE);

    assertThat(resultingDto).isEqualTo(expectedMaxResponseDto);
  }
}
