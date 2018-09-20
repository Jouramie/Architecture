package ca.ulaval.glo4003.ws.api.stock;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.*;

@SuppressWarnings("ALL")
@Path("/stock")
public interface StockResource {
    @GET
    @Path("/{title}")
    @Operation(summary = "Stock informations for given title", description = "Return the stock title, market, stock name, category, " + "stock value at market openning, current stock value and stock value at market close.", responses = {@ApiResponse(description = "Stock informations", content = @Content(schema = @Schema(implementation = StockDto.class))), @ApiResponse(responseCode = "400", description = "Missing title"), @ApiResponse(responseCode = "404", description = "Stock does not exist")})
    StockDto getStockByTitle(@Parameter(description = "Title", required = true) @PathParam("title") String title);

    @GET
    @Operation(summary = "Search stock informations by name", description = "Return the stock title, market, stock name, category, " + "stock value at market openning, current stock value and stock value at market close.", responses = {@ApiResponse(description = "Stock informations", content = @Content(schema = @Schema(implementation = StockDto.class))), @ApiResponse(responseCode = "400", description = "Missing name query parameter"), @ApiResponse(responseCode = "404", description = "Stock does not exist")})
    StockDto getStockByName(@Parameter(description = "Stock name", required = true) @QueryParam("name") String name);
}