package ca.ulaval.glo4003.ws.api.stock;

import ca.ulaval.glo4003.service.stock.StockDto;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/stocks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface StockResource {
  @GET
  @Path("/{title}")
  @Operation(summary = "Stock information for given title.",
      description = "Return the stock title, market, stock name, category, "
          + "stock value at market opening, current stock value and stock value at market close.",
      responses = {@ApiResponse(description = "Stock information", content = @Content(schema = @Schema(implementation = StockDto.class))),
          @ApiResponse(responseCode = "404", description = "Stock does not exist")})
  StockDto getStockByTitle(@Parameter(description = "Title", required = true) @PathParam("title") String title);

  @GET
  @Operation(summary = "Search stock information by name.",
      description = "Return the stock title, market, stock name, category, "
          + "stock value at market opening, current stock value and stock value at market close.",
      responses = {@ApiResponse(description = "Stock information", content = @Content(schema = @Schema(implementation = StockDto.class))),
          @ApiResponse(responseCode = "400", description = "Missing name query parameter"),
          @ApiResponse(responseCode = "404", description = "Stock does not exist")})
  StockDto getStockByName(@Parameter(description = "Stock name", required = true) @QueryParam("name") String name);
}
