package ca.ulaval.glo4003.ws.api.stock.trend;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/stocks/{title}/trend")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public interface StockTrendResource {

  @GET
  @Operation(summary = "Get the variation trend for a given stock title.",
      description = "Return the variation trend for the last 5 days, last 30 days, last year.",
      responses = {@ApiResponse(description = "Stock variation trend information",
          content = @Content(schema = @Schema(implementation = StockTrendDto.class))),
          @ApiResponse(responseCode = "200", description = "OK"),
          @ApiResponse(responseCode = "404", description = "Stock does not exist")
      })
  StockTrendDto getStockTrend(@Parameter(description = "Stock title", required = true)
                              @PathParam("title") String title);
}
