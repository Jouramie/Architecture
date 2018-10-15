package ca.ulaval.glo4003.ws.api.stock.trend;

import javax.annotation.Resource;

@Resource
public class StockTrendResourceImpl implements StockTrendResource {

  @Override
  public StockTrendDto getStockTrend(String title) {
    return new StockTrendDto("foo", StockTrend.INCREASING, StockTrend.DECREASING, StockTrend.STABLE);
  }
}
