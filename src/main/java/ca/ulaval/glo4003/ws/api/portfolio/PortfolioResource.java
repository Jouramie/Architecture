package ca.ulaval.glo4003.ws.api.portfolio;

import ca.ulaval.glo4003.ws.http.AuthenticationRequiredBinding;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
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
      summary = "Get the content of the portfolio.",
      description = "Return every stocks and their quantity in the user portfolio.",
      responses = {
          @ApiResponse(
              responseCode = "200", description = "portfolio content",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = PortfolioItemResponseDto.class))
              )
          ),
          @ApiResponse(responseCode = "401", description = "not logged in")
      }
  )
  List<PortfolioItemResponseDto> getPortfolioContent();
}
