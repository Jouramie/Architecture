package ca.ulaval.glo4003.ws.api.transaction;

import ca.ulaval.glo4003.service.date.SinceParameter;
import ca.ulaval.glo4003.ws.api.transaction.dto.ApiTransactionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;

public interface DocumentedStockTransactionResource {
  @Operation(
      summary = "Get transactions for a specific stock.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          implementation = ApiTransactionDto.class
                      )
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Stock title does not exist."
          ),
          @ApiResponse(
              responseCode = "401",
              description = "Administrator is not logged in."
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Malformed since parameter. Should be 'LAST_FIVE_DAYS' or 'LAST_THIRTY_DAYS'."
          )
      }
  )
  List<ApiTransactionDto> getStockTransactions(
      @Parameter(description = "Stock title") String title,
      @Parameter(
          schema = @Schema(implementation = SinceParameter.class)
      )
          String since);
}
