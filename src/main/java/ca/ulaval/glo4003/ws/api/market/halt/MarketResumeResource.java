package ca.ulaval.glo4003.ws.api.market.halt;

import ca.ulaval.glo4003.ws.api.market.dto.MarketStatusResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/markets/{market}/resume")
@Produces(MediaType.APPLICATION_JSON)
public interface MarketResumeResource {

  @POST
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
              responseCode = "400",
              description = "Market is not halted."
          ),
          @ApiResponse(
              responseCode = "401",
              description = "The authenticated user is not an administrator."
          )
      }
  )
  MarketStatusResponseDto haltMarket(@PathParam("market") String market);
}
