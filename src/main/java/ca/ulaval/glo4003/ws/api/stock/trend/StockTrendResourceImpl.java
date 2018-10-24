package ca.ulaval.glo4003.ws.api.stock.trend;

import ca.ulaval.glo4003.service.stock.trend.StockVariationTrendService;
import ca.ulaval.glo4003.service.stock.trend.dto.StockVariationSummary;
import javax.annotation.Resource;
import javax.inject.Inject;

@Resource
public class StockTrendResourceImpl implements StockTrendResource {

  private final StockVariationTrendService stockVariationTrendService;

  @Inject
  public StockTrendResourceImpl(StockVariationTrendService stockVariationTrendService) {
    this.stockVariationTrendService = stockVariationTrendService;
  }

  @Override
  public StockTrendDto getStockTrend(String title) {
    StockVariationSummary variationSummary = stockVariationTrendService.getStockVariationSummary(title);
    return new StockTrendDto(title,
        variationSummary.last5days,
        variationSummary.last30days,
        variationSummary.lastYear);
  }
}
