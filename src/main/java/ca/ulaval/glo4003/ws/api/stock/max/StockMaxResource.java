package ca.ulaval.glo4003.ws.api.stock.max;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/stocks/{title}/max")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public interface StockMaxResource {

  @GET
  @Operation(summary = "Get the maximum value for a given stock title.",
      description = "Return the stock maximum value in the requested time period.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  schema = @Schema(
                      implementation = ApiStockMaxResponseDto.class
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Stock does not exist."
          )
      }
  )
  ApiStockMaxResponseDto getStockMaxValue(@PathParam("title") String title);
}
