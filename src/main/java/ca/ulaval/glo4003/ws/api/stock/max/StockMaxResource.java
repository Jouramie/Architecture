package ca.ulaval.glo4003.ws.api.stock.max;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import ca.ulaval.glo4003.service.stock.max.StockMaxValueSinceRange;
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

@Path("/stocks/{title}/max")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public interface StockMaxResource {

  @GET
  @Operation(summary = "Get the maximum value for a given stock title.",
      description = "Return the stock maximum value since the interval asked.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  schema = @Schema(
                      implementation = StockMaxResponseDto.class
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "'since' parameter is missing or invalid."
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Stock does not exist."
          )
      }
  )
  StockMaxResponseDto getStockMaxValue(
      @PathParam("title")
          String title,
      @QueryParam("since")
      @Parameter(
          description = "Specify a range where the maximum value will be searched for.",
          schema = @Schema(
              implementation = StockMaxValueSinceRange.class
          ),
          required = true
      )
          String since);
}
