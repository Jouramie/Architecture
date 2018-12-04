package ca.ulaval.glo4003.ws.api.stock.resources;

import ca.ulaval.glo4003.service.stock.trend.StockVariationTrendService;
import ca.ulaval.glo4003.service.stock.trend.dto.StockVariationSummary;
import ca.ulaval.glo4003.ws.api.stock.dtos.ApiStockTrendDto;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/stocks/{title}/trend")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Resource
public class StockTrendResourceImpl implements DocumentedStockTrendResource {

  private final StockVariationTrendService stockVariationTrendService;

  @Inject
  public StockTrendResourceImpl(StockVariationTrendService stockVariationTrendService) {
    this.stockVariationTrendService = stockVariationTrendService;
  }

  @GET
  @Override
  public ApiStockTrendDto getStockTrend(@PathParam("title") String title) {
    StockVariationSummary variationSummary = stockVariationTrendService.getStockVariationSummary(title);
    return new ApiStockTrendDto(title,
        variationSummary.last5days,
        variationSummary.last30days,
        variationSummary.lastYear);
  }
}
