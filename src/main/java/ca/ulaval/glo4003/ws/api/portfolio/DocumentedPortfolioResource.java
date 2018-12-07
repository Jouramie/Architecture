package ca.ulaval.glo4003.ws.api.portfolio;

import ca.ulaval.glo4003.service.date.Since;
import ca.ulaval.glo4003.ws.api.portfolio.dto.ApiPortfolioReportResponseDto;
import ca.ulaval.glo4003.ws.api.portfolio.dto.ApiPortfolioResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface DocumentedPortfolioResource {

  @Operation(
      summary = "Get the content of the portfolio and its value.",
      description = "Return every stocks and current total value of the user's portfolio.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  schema = @Schema(
                      implementation = ApiPortfolioResponseDto.class
                  )
              )
          ),
          @ApiResponse(
              responseCode = "401",
              description = "Investor is not logged in."
          )
      }
  )
  ApiPortfolioResponseDto getPortfolio();

  @Operation(
      summary = "Get portfolio report with historical values and most volatile stocks.",
      description = "Return historical portfolio content, the most increasing and the most decreasing stocks.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  schema = @Schema(
                      implementation = ApiPortfolioReportResponseDto.class
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Missing 'since' query param."
          ),
          @ApiResponse(
              responseCode = "401",
              description = "User is not logged in."
          )
      }
  )
  ApiPortfolioReportResponseDto getPortfolioReport(
      @Parameter(
          description = "Report since 'LAST_FIVE_DAYS', 'LAST_THIRTY_DAYS' or 'LAST_YEAR'",
          content = @Content(
              schema = @Schema(implementation = Since.class)
          )
      )
          String since
  );
}
