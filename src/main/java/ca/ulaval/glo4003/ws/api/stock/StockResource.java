package ca.ulaval.glo4003.ws.api.stock;

import ca.ulaval.glo4003.service.stock.StockMaxValueSinceParameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.validation.constraints.NotNull;
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
      responses = {@ApiResponse(description = "Stock information",
          content = @Content(schema = @Schema(implementation = StockDto.class))),
          @ApiResponse(responseCode = "404", description = "Stock does not exist")})
  StockDto getStockByTitle(@Parameter(description = "Title", required = true)
                           @PathParam("title") String title);

  @GET
  @Operation(summary = "Search stock information by name.",
      description = "Return the stock title, market, stock name, category, "
          + "stock value at market opening, current stock value and stock value at market close.",
      responses = {@ApiResponse(description = "Stock information",
          content = @Content(schema = @Schema(implementation = StockDto.class))),
          @ApiResponse(responseCode = "400", description = "Missing name query parameter"),
          @ApiResponse(responseCode = "404", description = "Stock does not exist")})
  StockDto getStockByName(@Parameter(description = "Stock name", required = true)
                          @QueryParam("name") String name);

  @GET
  @Path("/{title}/max")
  @Operation(summary = "Get stock maximum value.",
      description = "Return the stock maximum value since the interval asked.",
      responses = {@ApiResponse(description = "Stock maximum value",
          content = @Content(schema = @Schema(implementation = StockMaxResponseDto.class))),
          @ApiResponse(responseCode = "400", description = "Missing or invalid since parameter"),
          @ApiResponse(responseCode = "404", description = "Stock does not exist")})
  StockMaxResponseDto getStockMaxValue(@Parameter(description = "Title of the stock", required = true)
                                       @PathParam("title") String title,
                                       @Parameter(description = "Since parameter",
                                           schema = @Schema(implementation = StockMaxValueSinceParameter.class),
                                           required = true)
                                       @QueryParam("since") @NotNull StockMaxValueSinceParameter since);
}
