package ca.ulaval.glo4003.service.stock.max;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockValueHistory;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockMaxValueRetrieverTest {
  private static final LocalDate OCTOBER15_2018 = LocalDate.of(2018, 10, 15);

  @Mock
  private Clock clock;
  @Mock
  private Stock stock;
  @Mock
  private StockValueHistory stockValueHistory;

  private StockMaxValueRetriever stockMaxValueRetriever;

  @Before
  public void setupStockMaxValueRetriever() {
    given(clock.getCurrentTime()).willReturn(OCTOBER15_2018.atTime(14, 0, 0));

    given(stock.getValueHistory()).willReturn(stockValueHistory);

    stockMaxValueRetriever = new StockMaxValueRetriever(clock);
  }

  @Test
  public void whenGetStockMaxValueWithLastFiveDays_thenGetMaxValueOfLastFiveDays() throws NoStockValueFitsCriteriaException {
    stockMaxValueRetriever.getStockMaxValue(stock, StockMaxValueSinceRange.LAST_FIVE_DAYS);

    verify(stockValueHistory).getMaxValue(LocalDate.of(2018, 10, 10), OCTOBER15_2018);
  }

  @Test
  public void whenGetStockMaxValueWithCurrentMonth_thenGetMaxValueOfCurrentMonth() throws NoStockValueFitsCriteriaException {
    stockMaxValueRetriever.getStockMaxValue(stock, StockMaxValueSinceRange.CURRENT_MONTH);

    verify(stockValueHistory).getMaxValue(LocalDate.of(2018, 10, 1), OCTOBER15_2018);
  }

  @Test
  public void whenGetStockMaxValueWithLastMonth_thenGetMaxValueOfLastMonth() throws NoStockValueFitsCriteriaException {
    stockMaxValueRetriever.getStockMaxValue(stock, StockMaxValueSinceRange.LAST_MONTH);

    verify(stockValueHistory).getMaxValue(LocalDate.of(2018, 9, 1), LocalDate.of(2018, 9, 30));
  }

  @Test
  public void whenGetStockMaxValueWithLastYear_thenGetMaxValueOfLastYear() throws NoStockValueFitsCriteriaException {
    stockMaxValueRetriever.getStockMaxValue(stock, StockMaxValueSinceRange.LAST_YEAR);

    verify(stockValueHistory).getMaxValue(LocalDate.of(2017, 10, 15), OCTOBER15_2018);
  }

  @Test
  public void whenGetStockMaxValueWithLastFiveYears_thenGetMaxValueOfLastFiveYears() throws NoStockValueFitsCriteriaException {
    stockMaxValueRetriever.getStockMaxValue(stock, StockMaxValueSinceRange.LAST_FIVE_YEARS);

    verify(stockValueHistory).getMaxValue(LocalDate.of(2013, 10, 15), OCTOBER15_2018);
  }

  @Test
  public void whenGetStockMaxValueWithLastTenYears_thenGetMaxValueOfLastTenYears() throws NoStockValueFitsCriteriaException {
    stockMaxValueRetriever.getStockMaxValue(stock, StockMaxValueSinceRange.LAST_TEN_YEARS);

    verify(stockValueHistory).getMaxValue(LocalDate.of(2008, 10, 15), OCTOBER15_2018);
  }

  @Test
  public void whenGetStockMaxValueWithAllTime_thenGetMaxValueOfAllTime() throws NoStockValueFitsCriteriaException {
    stockMaxValueRetriever.getStockMaxValue(stock, StockMaxValueSinceRange.ALL_TIME);

    verify(stockValueHistory).getMaxValue(LocalDate.MIN, OCTOBER15_2018);
  }
}
