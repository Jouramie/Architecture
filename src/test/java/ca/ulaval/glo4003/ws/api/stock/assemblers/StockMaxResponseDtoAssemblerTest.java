package ca.ulaval.glo4003.ws.api.stock.assemblers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.domain.stock.StockValue;
import ca.ulaval.glo4003.service.stock.max.dto.StockMaxValueSummary;
import ca.ulaval.glo4003.ws.api.stock.dtos.StockMaxResponseDto;
import ca.ulaval.glo4003.ws.api.stock.dtos.StockMaxResponseValueDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockMaxResponseDtoAssemblerTest {
  private final String SOME_TITLE = "MSFT";
  private final LocalDate SOME_DATE = LocalDate.of(2018, 10, 20);
  private final MoneyAmount SOME_MAX_AMOUNT = new MoneyAmount(20.00, new Currency("CAD", new BigDecimal(0.75)));
  private final LocalDate LAST_FIVE_DAYS_DATE = LocalDate.of(2018, 10, 19);
  private final LocalDate CURRENT_MONTH_DATE = LocalDate.of(2018, 10, 18);
  private final LocalDate LAST_MONTH_DATE = LocalDate.of(2018, 10, 17);
  private final LocalDate LAST_YEAR_DATE = LocalDate.of(2018, 10, 16);
  private final LocalDate LAST_FIVE_YEARS_DATE = LocalDate.of(2018, 10, 15);
  private final LocalDate LAST_TEN_YEARS_DATE = LocalDate.of(2018, 10, 14);
  private final LocalDate ALL_TIME_DATE = LocalDate.of(2018, 10, 13);

  @Mock
  private StockValue SOME_VALUE;

  private StockMaxResponseDtoAssembler assembler;

  @Before
  public void setupStockMaxResponseDtoAssembler() {
    assembler = new StockMaxResponseDtoAssembler();

    given(SOME_VALUE.getMaximumValue()).willReturn(SOME_MAX_AMOUNT);
  }

  @Test
  public void givenHistoricalStockValue_whenToDto_thenConvertToDtoWithUsdMaxValue() {
    HistoricalStockValue historicalStockValue = new HistoricalStockValue(SOME_DATE, SOME_VALUE);

    StockMaxResponseValueDto dto = assembler.toDto(historicalStockValue);

    assertThat(dto.maximumValueDate).isEqualTo(SOME_DATE);
    assertThat(dto.maximumValue).isEqualTo(SOME_MAX_AMOUNT.toUsd());
  }

  @Test
  public void givenStockMaxValueSummary_whenToDto_thenConvertToDtoWithMatchingDates() {
    HistoricalStockValue lastFive = new HistoricalStockValue(SOME_DATE, SOME_VALUE);
    StockMaxValueSummary summary = new StockMaxValueSummary(
        new HistoricalStockValue(LAST_FIVE_DAYS_DATE, SOME_VALUE),
        new HistoricalStockValue(CURRENT_MONTH_DATE, SOME_VALUE),
        new HistoricalStockValue(LAST_MONTH_DATE, SOME_VALUE),
        new HistoricalStockValue(LAST_YEAR_DATE, SOME_VALUE),
        new HistoricalStockValue(LAST_FIVE_YEARS_DATE, SOME_VALUE),
        new HistoricalStockValue(LAST_TEN_YEARS_DATE, SOME_VALUE),
        new HistoricalStockValue(ALL_TIME_DATE, SOME_VALUE)
    );

    StockMaxResponseDto dto = assembler.toDto(SOME_TITLE, summary);

    assertThat(dto.title).isEqualTo(SOME_TITLE);
    assertThat(dto.lastFiveDays.maximumValueDate).isEqualTo(LAST_FIVE_DAYS_DATE);
    assertThat(dto.currentMonth.maximumValueDate).isEqualTo(CURRENT_MONTH_DATE);
    assertThat(dto.lastMonth.maximumValueDate).isEqualTo(LAST_MONTH_DATE);
    assertThat(dto.lastYear.maximumValueDate).isEqualTo(LAST_YEAR_DATE);
    assertThat(dto.lastFiveYears.maximumValueDate).isEqualTo(LAST_FIVE_YEARS_DATE);
    assertThat(dto.lastTenYears.maximumValueDate).isEqualTo(LAST_TEN_YEARS_DATE);
    assertThat(dto.allTime.maximumValueDate).isEqualTo(ALL_TIME_DATE);
  }
}
