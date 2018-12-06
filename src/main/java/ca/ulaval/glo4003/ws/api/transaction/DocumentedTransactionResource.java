package ca.ulaval.glo4003.ws.api.transaction;

import ca.ulaval.glo4003.ws.api.transaction.dto.TransactionModelDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;

public interface DocumentedTransactionResource {
  @Operation(
      summary = "Get transactions for all users.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          implementation = TransactionModelDto.class
                      )
                  )
              )
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
  List<TransactionModelDto> getTransactions(
      @Parameter(description = "History since. 'LAST_FIVE_DAYS' or 'LAST_THIRTY_DAYS'") String since);

  @Operation(
      summary = "Get transactions for a specific user.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          implementation = TransactionModelDto.class
                      )
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "User does not exist."
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
  List<TransactionModelDto> getUserTransactions(
      @Parameter(description = "User's email") String email,
      @Parameter(description = "History since. 'LAST_FIVE_DAYS' or 'LAST_THIRTY_DAYS'") String since);

  @Operation(
      summary = "Get transactions for a specific stock.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          implementation = TransactionModelDto.class
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
  List<TransactionModelDto> getStockTransactions(
      @Parameter(description = "Stock title") String title,
      @Parameter(description = "History since. 'LAST_FIVE_DAYS' or 'LAST_THIRTY_DAYS'") String since);
}
