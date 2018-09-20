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
            summary = "Stock informations",
            description = "Return the stock informations for given title.",
            responses = {
                    @ApiResponse(
                            description = "Stock value",
                            content = @Content(schema = @Schema(implementation = StockDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Missing title"
                    )
            }
    )
    StockDto getStockByTitle(
            @Parameter(
                    description = "title stock",
                    required = true
            )
            @PathParam("title") String title
    );
}