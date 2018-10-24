package ca.ulaval.glo4003.ws.api.stock.max;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.service.stock.StockService;
import ca.ulaval.glo4003.service.stock.max.StockMaxValueSinceRange;
import javax.ws.rs.BadRequestException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockMaxResourceTest {
  private static final String SOME_TITLE = "title";
  private static final StockMaxValueSinceRange SOME_RANGE = StockMaxValueSinceRange.LAST_FIVE_DAYS;

  @Mock
  private StockService stockService;
  @Mock
  private StockMaxResponseDto expectedMaxResponseDto;

  @InjectMocks
  private StockMaxResourceImpl stockResource;

  @Test
  public void whenGetStockMaxValue_thenReturningDto() {
    given(stockService.getStockMaxValue(SOME_TITLE, SOME_RANGE)).willReturn(expectedMaxResponseDto);

    StockMaxResponseDto resultingDto = stockResource.getStockMaxValue(SOME_TITLE, SOME_RANGE.toString());

    assertThat(resultingDto).isEqualTo(expectedMaxResponseDto);
  }

  @Test
  public void givenInvalidSinceParameter_whenGetStockMax_thenThrowBadRequest() {
    String invalidSinceParameter = "invalid";

    ThrowingCallable getStockMax = () -> stockResource.getStockMaxValue(SOME_TITLE, invalidSinceParameter);

    assertThatThrownBy(getStockMax).isInstanceOf(BadRequestException.class);
  }
}
