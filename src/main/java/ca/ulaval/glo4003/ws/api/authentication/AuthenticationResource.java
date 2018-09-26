package ca.ulaval.glo4003.ws.api.authentication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/authenticate")
public interface AuthenticationResource {

  @POST
  @Operation(
      summary = "User login",
      description = "Request a personal identification token.",
      responses = {
          @ApiResponse(
              responseCode = "200", content = @Content(schema = @Schema(implementation = AuthenticationResponseDto.class))
          ),
          @ApiResponse(
              responseCode = "400", description = "wrong user password"
          )
      }
  )
  AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequest);
}
