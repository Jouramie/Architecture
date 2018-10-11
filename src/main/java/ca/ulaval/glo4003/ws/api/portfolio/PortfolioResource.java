package ca.ulaval.glo4003.ws.api.portfolio;

import ca.ulaval.glo4003.ws.http.AuthenticationRequiredBinding;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/portfolio")
@AuthenticationRequiredBinding
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PortfolioResource {
  @GET
  @Operation(
      summary = "Get the content of the portfolio and its value.",
      description = "Return stocks and current total value of the user portfolio.",
      responses = {
          @ApiResponse(
              responseCode = "200", description = "portfolio",
              content = @Content(schema = @Schema(implementation = PortfolioResponseDto.class))
          ),
          @ApiResponse(responseCode = "401", description = "not logged in")
      }
  )
  PortfolioResponseDto getPortfolio();
}
