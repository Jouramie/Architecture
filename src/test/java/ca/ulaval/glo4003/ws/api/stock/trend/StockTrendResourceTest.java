package ca.ulaval.glo4003.ws.api.stock.trend;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.stock.StockTrend;
import ca.ulaval.glo4003.service.stock.trend.StockVariationTrendService;
import ca.ulaval.glo4003.service.stock.trend.dto.StockVariationSummary;
import ca.ulaval.glo4003.ws.api.stock.dtos.ApiStockTrendDto;
import ca.ulaval.glo4003.ws.api.stock.resources.StockTrendResourceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockTrendResourceTest {

  private static final String STOCK_TITLE = "RBS.l";
  private static final StockVariationSummary STOCK_VARIATION_SUMMARY =
      new StockVariationSummary(StockTrend.INCREASING, StockTrend.INCREASING, StockTrend.INCREASING);

  @Mock
  private StockVariationTrendService stockVariationTrendService;

  private StockTrendResourceImpl resource;

  @Before
  public void setupStockTrendResource() {
    resource = new StockTrendResourceImpl(stockVariationTrendService);
  }

  @Test
  public void whenGettingStockVariationTrend_thenReturnGeneratedSummary() {
    given(stockVariationTrendService.getStockVariationSummary(STOCK_TITLE)).willReturn(STOCK_VARIATION_SUMMARY);

    ApiStockTrendDto stockTrend = resource.getStockTrend(STOCK_TITLE);

    assertEquals(STOCK_TITLE, stockTrend.title);
    assertEquals(STOCK_VARIATION_SUMMARY.last5days, stockTrend.last5Days);
    assertEquals(STOCK_VARIATION_SUMMARY.last30days, stockTrend.last30Days);
    assertEquals(STOCK_VARIATION_SUMMARY.lastYear, stockTrend.lastYear);
  }
}
