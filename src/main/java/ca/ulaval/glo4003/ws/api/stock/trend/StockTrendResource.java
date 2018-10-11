package ca.ulaval.glo4003.ws.api.stock.trend;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import io.swagger.v3.oas.annotations.Parameter;
import javax.ws.rs.*;

@Path("/stocks/{title}/trend")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public interface StockTrendResource {

  @GET
  StockTrendDto getStockTrend(@Parameter(description = "Stock title", required = true)
                              @PathParam("title") String title);
}
