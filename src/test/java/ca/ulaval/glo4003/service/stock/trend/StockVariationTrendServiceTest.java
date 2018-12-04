package ca.ulaval.glo4003.service.stock.trend;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.query.StockQuery;
import ca.ulaval.glo4003.domain.stock.query.StockQueryBuilder;
import ca.ulaval.glo4003.service.date.DateService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockVariationTrendServiceTest {

  public static final String STOCK_TITLE = "RBS.l";
  private final Clock clock = new Clock(LocalDateTime.of(1984, Month.JANUARY, 24, 10, 0), Duration.ZERO);
  @Mock
  private StockRepository stockRepositoryMock;
  @Mock
  private StockVariationCalculator stockVariationCalculatorMock;
  private DateService dateService;
  private StockVariationTrendService stockVariationTrendService;

  @Before
  public void initialize() {
    dateService = new DateService(clock);
    stockVariationTrendService = new StockVariationTrendService(stockRepositoryMock, dateService, stockVariationCalculatorMock);
  }

  @Test
  public void whenGettingStockVariationSummary_thenFetchStockAndSendsToCalculator() {
    StockQuery stockQuery = new StockQueryBuilder().withTitle(STOCK_TITLE).build();
    given(stockRepositoryMock.find(stockQuery)).willReturn(Arrays.asList(mock(Stock.class)));

    stockVariationTrendService.getStockVariationSummary(STOCK_TITLE);

    verify(stockRepositoryMock).find(stockQuery);
    verify(stockVariationCalculatorMock).getStockVariationTrendSinceDate(any(), eq(dateService.getFiveDaysAgo()));
    verify(stockVariationCalculatorMock).getStockVariationTrendSinceDate(any(), eq(dateService.getThirtyDaysAgo()));
    verify(stockVariationCalculatorMock).getStockVariationTrendSinceDate(any(), eq(dateService.getOneYearAgo()));
  }
}
