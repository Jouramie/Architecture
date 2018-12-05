package ca.ulaval.glo4003.service.stock.max;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import ca.ulaval.glo4003.domain.clock.ReadableClock;
import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockHistory;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.service.date.DateService;
import ca.ulaval.glo4003.service.stock.StockDoesNotExistException;
import ca.ulaval.glo4003.service.stock.max.dto.StockMaxValueSummary;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockMaxValueServiceTest {
  private final String SOME_TITLE = "MSFT";
  private final LocalDate CURRENT_DATE = LocalDate.of(2018, 10, 20);
  private final LocalDate FIVE_DAYS_AGO = LocalDate.of(2018, 10, 19);
  private final LocalDate START_OF_MONTH = LocalDate.of(2018, 10, 18);
  private final LocalDate THIRTY_DAYS_AGO = LocalDate.of(2018, 10, 17);
  private final LocalDate ONE_YEAR_AGO = LocalDate.of(2018, 10, 16);
  private final LocalDate FIVE_YEARS_AGO = LocalDate.of(2018, 10, 15);
  private final LocalDate TEN_YEARS_AGO = LocalDate.of(2018, 10, 14);

  @Mock
  StockRepository stockRepository;
  @Mock
  DateService dateService;
  @Mock
  ReadableClock clock;
  @Mock
  Stock givenStock;
  @Mock
  StockHistory givenStockHistory;

  private StockMaxValueService stockMaxValueService;

  @Before
  public void setupStockMaxValueService() {
    stockMaxValueService = new StockMaxValueService(stockRepository, dateService, clock);
  }

  @Test
  public void whenGetStockMaxValue_thenSummaryContainsMaxValuesOfCorrespondingDateRanges()
      throws StockNotFoundException, NoStockValueFitsCriteriaException {
    given(stockRepository.findByTitle(SOME_TITLE)).willReturn(givenStock);
    given(clock.getCurrentDate()).willReturn(CURRENT_DATE);

    given(dateService.getFiveDaysAgo()).willReturn(FIVE_DAYS_AGO);
    given(dateService.getStartOfCurrentMonth()).willReturn(START_OF_MONTH);
    given(dateService.getThirtyDaysAgo()).willReturn(THIRTY_DAYS_AGO);
    given(dateService.getOneYearAgo()).willReturn(ONE_YEAR_AGO);
    given(dateService.getFiveYearsAgo()).willReturn(FIVE_YEARS_AGO);
    given(dateService.getTenYearsAgo()).willReturn(TEN_YEARS_AGO);

    given(givenStock.getValueHistory()).willReturn(givenStockHistory);
    HistoricalStockValue lastFiveDaysValue = mock(HistoricalStockValue.class);
    given(givenStockHistory.getMaxValue(FIVE_DAYS_AGO, CURRENT_DATE))
        .willReturn(Optional.of(lastFiveDaysValue));
    HistoricalStockValue currentMonthValue = mock(HistoricalStockValue.class);
    given(givenStockHistory.getMaxValue(START_OF_MONTH, CURRENT_DATE))
        .willReturn(Optional.of(currentMonthValue));
    HistoricalStockValue lastMonthValue = mock(HistoricalStockValue.class);
    given(givenStockHistory.getMaxValue(THIRTY_DAYS_AGO, CURRENT_DATE))
        .willReturn(Optional.of(lastMonthValue));
    HistoricalStockValue lastYearValue = mock(HistoricalStockValue.class);
    given(givenStockHistory.getMaxValue(ONE_YEAR_AGO, CURRENT_DATE))
        .willReturn(Optional.of(lastYearValue));
    HistoricalStockValue lastFiveYearsValue = mock(HistoricalStockValue.class);
    given(givenStockHistory.getMaxValue(FIVE_YEARS_AGO, CURRENT_DATE))
        .willReturn(Optional.of(lastFiveYearsValue));
    HistoricalStockValue lastTenYearsValue = mock(HistoricalStockValue.class);
    given(givenStockHistory.getMaxValue(TEN_YEARS_AGO, CURRENT_DATE))
        .willReturn(Optional.of(lastTenYearsValue));
    HistoricalStockValue allTimeValue = mock(HistoricalStockValue.class);
    given(givenStockHistory.getMaxValue(LocalDate.MIN, CURRENT_DATE))
        .willReturn(Optional.of(allTimeValue));

    StockMaxValueSummary summary = stockMaxValueService.getStockMaxValue(SOME_TITLE);

    assertThat(summary.lastFiveDays).isSameAs(lastFiveDaysValue);
    assertThat(summary.currentMonth).isSameAs(currentMonthValue);
    assertThat(summary.lastMonth).isSameAs(lastMonthValue);
    assertThat(summary.lastYear).isSameAs(lastYearValue);
    assertThat(summary.lastFiveYears).isSameAs(lastFiveYearsValue);
    assertThat(summary.lastTenYears).isSameAs(lastTenYearsValue);
    assertThat(summary.allTime).isSameAs(allTimeValue);
  }

  @Test
  public void givenStockDoesNotExist_whenGetStockMaxValue_thenStockDoesNotExistExceptionIsThrown()
      throws StockNotFoundException {
    doThrow(StockNotFoundException.class).when(stockRepository).findByTitle(any());

    assertThatThrownBy(() -> stockMaxValueService.getStockMaxValue(SOME_TITLE))
        .isInstanceOf(StockDoesNotExistException.class);
  }
}
