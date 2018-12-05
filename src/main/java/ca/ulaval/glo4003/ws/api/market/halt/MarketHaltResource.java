package ca.ulaval.glo4003.ws.api.market.halt;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.service.market.MarketDoesNotExistException;
import ca.ulaval.glo4003.ws.api.market.dto.MarketStatusResponseDto;
import ca.ulaval.glo4003.ws.http.authentication.AuthenticationRequiredBinding;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/markets/{market}")
@Produces(MediaType.APPLICATION_JSON)
public interface MarketHaltResource {

  @POST
  @Path("/halt")
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
  @AuthenticationRequiredBinding(authorizedRoles = UserRole.ADMINISTRATOR)
  MarketStatusResponseDto haltMarket(@PathParam("market") String market, @QueryParam("message") String message) throws MarketDoesNotExistException;

  @POST
  @Path("/resume")
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
  @AuthenticationRequiredBinding(authorizedRoles = UserRole.ADMINISTRATOR)
  MarketStatusResponseDto resumeMarket(@PathParam("market") String market) throws MarketDoesNotExistException;
}
