package ca.ulaval.glo4003.ws.api.stock.resources;

import ca.ulaval.glo4003.ws.api.stock.dtos.ApiStockTrendDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface DocumentedStockTrendResource {

  @Operation(
      summary = "Get the variation trend for a given stock title.",
      description = "Return the variation trend for the last 5 days, last 30 days, last year.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  schema = @Schema(
                      implementation = ApiStockTrendDto.class
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Stock does not exist."
          )
      }
  )
  ApiStockTrendDto getStockTrend(String title);
}
