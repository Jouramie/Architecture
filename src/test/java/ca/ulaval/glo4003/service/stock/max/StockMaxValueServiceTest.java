package ca.ulaval.glo4003.service.stock.max;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValueHistory;
import ca.ulaval.glo4003.service.stock.StockDoesNotExistException;
import ca.ulaval.glo4003.service.stock.max.dto.StockMaxValueSummary;
import ca.ulaval.glo4003.service.time.HistoricalDateService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockMaxValueServiceTest {
  private final String SOME_TITLE = "MSFT";
  private final LocalDateTime CURRENT_TIME = LocalDateTime.of(2018, 10, 20, 14, 0, 0);
  private final LocalDate FIVE_DAYS_AGO = LocalDate.of(2018, 10, 19);
  private final LocalDate START_OF_MONTH = LocalDate.of(2018, 10, 18);
  private final LocalDate THIRTY_DAYS_AGO = LocalDate.of(2018, 10, 17);
  private final LocalDate ONE_YEAR_AGO = LocalDate.of(2018, 10, 16);
  private final LocalDate FIVE_YEARS_AGO = LocalDate.of(2018, 10, 15);
  private final LocalDate TEN_YEARS_AGO = LocalDate.of(2018, 10, 14);

  @Mock
  StockRepository stockRepository;
  @Mock
  HistoricalDateService historicalDateService;
  @Mock
  Clock clock;
  @Mock
  Stock givenStock;
  @Mock
  StockValueHistory givenStockValueHistory;

  private StockMaxValueService stockMaxValueService;

  @Before
  public void setupStockMaxValueService() {
    stockMaxValueService = new StockMaxValueService(stockRepository, historicalDateService, clock);
  }

  @Test
  public void whenGetStockMaxValue_thenSummaryContainsMaxValuesOfCorrespondingDateRanges()
      throws StockNotFoundException, NoStockValueFitsCriteriaException {
    given(stockRepository.findByTitle(SOME_TITLE)).willReturn(givenStock);
    given(clock.getCurrentTime()).willReturn(CURRENT_TIME);

    given(historicalDateService.getFiveDaysAgo()).willReturn(FIVE_DAYS_AGO);
    given(historicalDateService.getStartOfCurrentMonth()).willReturn(START_OF_MONTH);
    given(historicalDateService.getThirtyDaysAgo()).willReturn(THIRTY_DAYS_AGO);
    given(historicalDateService.getOneYearAgo()).willReturn(ONE_YEAR_AGO);
    given(historicalDateService.getFiveYearsAgo()).willReturn(FIVE_YEARS_AGO);
    given(historicalDateService.getTenYearsAgo()).willReturn(TEN_YEARS_AGO);

    given(givenStock.getValueHistory()).willReturn(givenStockValueHistory);
    HistoricalStockValue lastFiveDaysValue = mock(HistoricalStockValue.class);
    given(givenStockValueHistory.getMaxValue(FIVE_DAYS_AGO, CURRENT_TIME.toLocalDate()))
        .willReturn(lastFiveDaysValue);
    HistoricalStockValue currentMonthValue = mock(HistoricalStockValue.class);
    given(givenStockValueHistory.getMaxValue(START_OF_MONTH, CURRENT_TIME.toLocalDate()))
        .willReturn(currentMonthValue);
    HistoricalStockValue lastMonthValue = mock(HistoricalStockValue.class);
    given(givenStockValueHistory.getMaxValue(THIRTY_DAYS_AGO, CURRENT_TIME.toLocalDate()))
        .willReturn(lastMonthValue);
    HistoricalStockValue lastYearValue = mock(HistoricalStockValue.class);
    given(givenStockValueHistory.getMaxValue(ONE_YEAR_AGO, CURRENT_TIME.toLocalDate()))
        .willReturn(lastYearValue);
    HistoricalStockValue lastFiveYearsValue = mock(HistoricalStockValue.class);
    given(givenStockValueHistory.getMaxValue(FIVE_YEARS_AGO, CURRENT_TIME.toLocalDate()))
        .willReturn(lastFiveYearsValue);
    HistoricalStockValue lastTenYearsValue = mock(HistoricalStockValue.class);
    given(givenStockValueHistory.getMaxValue(TEN_YEARS_AGO, CURRENT_TIME.toLocalDate()))
        .willReturn(lastTenYearsValue);
    HistoricalStockValue allTimeValue = mock(HistoricalStockValue.class);
    given(givenStockValueHistory.getMaxValue(LocalDate.MIN, CURRENT_TIME.toLocalDate()))
        .willReturn(allTimeValue);

    StockMaxValueSummary summary = stockMaxValueService.getStockMaxValue(SOME_TITLE);

    assertThat(summary.lastFiveDays).isEqualTo(lastFiveDaysValue);
    assertThat(summary.currentMonth).isEqualTo(currentMonthValue);
    assertThat(summary.lastMonth).isEqualTo(lastMonthValue);
    assertThat(summary.lastYear).isEqualTo(lastYearValue);
    assertThat(summary.lastFiveYears).isEqualTo(lastFiveYearsValue);
    assertThat(summary.lastTenYears).isEqualTo(lastTenYearsValue);
    assertThat(summary.allTime).isEqualTo(allTimeValue);
  }

  @Test
  public void givenStockDoesNotExist_whenGetStockMaxValue_thenStockDoesNotExistExceptionIsThrown()
      throws StockNotFoundException {
    doThrow(StockNotFoundException.class).when(stockRepository).findByTitle(any());

    assertThatThrownBy(() -> stockMaxValueService.getStockMaxValue(SOME_TITLE))
        .isInstanceOf(StockDoesNotExistException.class);
  }
}
