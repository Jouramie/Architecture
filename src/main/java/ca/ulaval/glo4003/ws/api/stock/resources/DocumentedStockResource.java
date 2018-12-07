package ca.ulaval.glo4003.ws.api.stock.resources;

import ca.ulaval.glo4003.ws.api.stock.dtos.ApiStockDto;
import ca.ulaval.glo4003.ws.api.transaction.dto.TransactionModelDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;

public interface DocumentedStockResource {

  @Operation(
      summary = "Get a stock for a given title.",
      description = "Return the details of the stock with the corresponding title.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  schema = @Schema(
                      implementation = ApiStockDto.class
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Stock does not exist."
          )
      }
  )
  ApiStockDto getStockByTitle(String title);

  @Operation(
      summary = "Get all stocks.",
      description = "Return all stocks paginated, with their information. Query parameters can be "
          + "used to filter the stocks.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              headers = {
                  @Header(
                      name = "X-Total-Count",
                      description = "The number of available stock.")
              },
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          implementation = ApiStockDto.class
                      )
                  )
              )
          )
      }
  )
  List<ApiStockDto> getStocks(
      @Parameter(description = "Search stock by name.")
          String name,
      @Parameter(description = "Search stock by category.")
          String category,
      @Parameter(
          description = "The page to display",
          schema = @Schema(
              defaultValue = "1"
          )
      )
          int page,
      @Parameter(
          description = "The number of stock per page",
          schema = @Schema(
              defaultValue = "15"
          )
      )
          int perPage);

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
