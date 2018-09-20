package ca.ulaval.glo4003.ws.api.stock;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.*;

@Path("/stock")
public interface StockResource {
    @GET
    @Path("/title/{title}")
    @Operation(
            summary = "Stock informations for given title",
            description = "Return the stock title, market, stock name, category, " +
                    "stock value at market openning, current stock value and stock value at market close.",
            responses = {
                    @ApiResponse(
                            description = "Stock informations",
                            content = @Content(schema = @Schema(implementation = StockDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Missing title"
                    )
            }
    )
    StockDto getStockByTitle(
            @Parameter(
                    description = "Title",
                    required = true
            )
            @PathParam("title") String title
    );

    @GET
    @Path("/stockName/{stockName}")
    @Operation(
            summary = "Stock informations for given stock name",
            description = "Return the stock title, market, stock name, category, " +
                    "stock value at market openning, current stock value and stock value at market close.",
            responses = {
                    @ApiResponse(
                            description = "Stock informations",
                            content = @Content(schema = @Schema(implementation = StockDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Missing stock name"
                    )
            }
    )
    StockDto getStockByStockName(
            @Parameter(
                    description = "Stock name",
                    required = true
            )
            @PathParam("stockName") String stockName
    );
}