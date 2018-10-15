package ca.ulaval.glo4003.service.stock;

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
public class MaximumValueStockRetrieverTest {
  private static final LocalDate SOME_DATE = LocalDate.of(2018, 10, 15);

  @Mock
  private Clock clock;
  @Mock
  private Stock stock;
  @Mock
  private StockValueHistory stockValueHistory;

  private MaximumValueStockRetriever maximumValueStockRetriever;

  @Before
  public void setupMaximumValueStockRetriever() {
    given(clock.getCurrentTime()).willReturn(SOME_DATE.atTime(14, 0, 0));

    given(stock.getValueHistory()).willReturn(stockValueHistory);

    maximumValueStockRetriever = new MaximumValueStockRetriever(clock);
  }

  @Test
  public void whenGetStockMaxValueWithLastFiveDays_thenGetMaxValueOfLastFiveDays() throws NoStockValueFitsCriteriaException {
    maximumValueStockRetriever.getStockMaxValue(stock, StockMaxValueSinceParameter.LAST_FIVE_DAYS);

    verify(stockValueHistory).getMaxValue(LocalDate.of(2018, 10, 10), SOME_DATE);
  }

  @Test
  public void whenGetStockMaxValueWithCurrentMonth_thenGetMaxValueOfCurrentMonth() throws NoStockValueFitsCriteriaException {
    maximumValueStockRetriever.getStockMaxValue(stock, StockMaxValueSinceParameter.CURRENT_MONTH);

    verify(stockValueHistory).getMaxValue(LocalDate.of(2018, 10, 1), SOME_DATE);
  }

  @Test
  public void whenGetStockMaxValueWithLastMonth_thenGetMaxValueOfLastMonth() throws NoStockValueFitsCriteriaException {
    maximumValueStockRetriever.getStockMaxValue(stock, StockMaxValueSinceParameter.LAST_MONTH);

    verify(stockValueHistory).getMaxValue(LocalDate.of(2018, 9, 1), LocalDate.of(2018, 9, 30));
  }

  @Test
  public void whenGetStockMaxValueWithLastYear_thenGetMaxValueOfLastYear() throws NoStockValueFitsCriteriaException {
    maximumValueStockRetriever.getStockMaxValue(stock, StockMaxValueSinceParameter.LAST_YEAR);

    verify(stockValueHistory).getMaxValue(LocalDate.of(2017, 10, 15), SOME_DATE);
  }

  @Test
  public void whenGetStockMaxValueWithLastFiveYears_thenGetMaxValueOfLastFiveYears() throws NoStockValueFitsCriteriaException {
    maximumValueStockRetriever.getStockMaxValue(stock, StockMaxValueSinceParameter.LAST_FIVE_YEARS);

    verify(stockValueHistory).getMaxValue(LocalDate.of(2013, 10, 15), SOME_DATE);
  }

  @Test
  public void whenGetStockMaxValueWithLastTenYears_thenGetMaxValueOfLastTenYears() throws NoStockValueFitsCriteriaException {
    maximumValueStockRetriever.getStockMaxValue(stock, StockMaxValueSinceParameter.LAST_TEN_YEARS);

    verify(stockValueHistory).getMaxValue(LocalDate.of(2008, 10, 15), SOME_DATE);
  }

  @Test
  public void whenGetStockMaxValueWithAllTime_thenGetMaxValueOfAllTime() throws NoStockValueFitsCriteriaException {
    maximumValueStockRetriever.getStockMaxValue(stock, StockMaxValueSinceParameter.ALL_TIME);

    verify(stockValueHistory).getMaxValue(LocalDate.MIN, SOME_DATE);
  }
}
