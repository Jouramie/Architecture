package ca.ulaval.glo4003.ws.api.stock.resources;

import ca.ulaval.glo4003.ws.api.stock.dtos.StockMaxResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface DocumentedStockMaxResource {

  @Operation(summary = "Get the maximum value for a given stock title.",
      description = "Return the stock maximum value in the requested time period.",
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
              responseCode = "404",
              description = "Stock does not exist."
          )
      }
  )
  StockMaxResponseDto getStockMaxValue(String title);
}
