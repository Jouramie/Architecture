package ca.ulaval.glo4003.service.stock.max;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.domain.stock.StockValue;
import ca.ulaval.glo4003.ws.api.stock.max.StockMaxResponseDto;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockMaxResponseAssemblerTest {
  private static final String SOME_TITLE = "someTitle";
  private static final LocalDate SOME_DATE = LocalDate.now();
  private static final MoneyAmount SOME_VALUE = new MoneyAmount(10.00, Currency.USD);

  @Mock
  private StockValue someStockValue;

  private StockMaxResponseAssembler stockMaxResponseDtoAssembler;

  @Before
  public void setUpStock() {
    stockMaxResponseDtoAssembler = new StockMaxResponseAssembler();
  }

  @Test
  public void whenAssemblerToDto_thenStockMaxResponseDtoIsCreated() {
    given(someStockValue.getMaximumValue()).willReturn(SOME_VALUE);
    StockValue someStockValue = new StockValue(SOME_VALUE);
    HistoricalStockValue historicalStockValue = new HistoricalStockValue(SOME_DATE, someStockValue);

    StockMaxResponseDto dto = stockMaxResponseDtoAssembler.toDto(SOME_TITLE, historicalStockValue);

    assertThat(dto.title).isEqualTo(SOME_TITLE);
    assertThat(dto.maximumValueDate).isEqualTo(SOME_DATE);
    assertThat(dto.maximumValue).isEqualTo(SOME_VALUE.toUsd());
  }
}
