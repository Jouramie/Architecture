package ca.ulaval.glo4003.ws.api.market;

import ca.ulaval.glo4003.service.market.MarketDoesNotExistException;
import ca.ulaval.glo4003.ws.api.market.dto.MarketStatusResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface DocumentedMarketHaltResource {

  @Operation(
      summary = "Puts a market in trading halt.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  schema = @Schema(
                      implementation = MarketStatusResponseDto.class
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Market does not exist."
          ),
          @ApiResponse(
              responseCode = "401",
              description = "The authenticated user is not an administrator."
          )
      }
  )
  MarketStatusResponseDto haltMarket(String market, String message) throws MarketDoesNotExistException;

  @Operation(
      summary = "Resumes trading in a halted market.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  schema = @Schema(
                      implementation = MarketStatusResponseDto.class
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Market does not exist."
          ),
          @ApiResponse(
              responseCode = "401",
              description = "The authenticated user is not an administrator."
          )
      }
  )
  MarketStatusResponseDto resumeMarket(String market) throws MarketDoesNotExistException;
}
