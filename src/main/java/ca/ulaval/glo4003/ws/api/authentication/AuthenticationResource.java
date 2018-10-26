package ca.ulaval.glo4003.ws.api.authentication;

import ca.ulaval.glo4003.ws.http.AuthenticationRequiredBinding;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public interface AuthenticationResource {

  @POST
  @Path("/authenticate")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(
      summary = "User login",
      description = "Request a personal identification token.",
      responses = {
          @ApiResponse(
              responseCode = "202",
              description = "Successfully authenticated.",
              content = @Content(
                  schema = @Schema(
                      implementation = ApiAuthenticationResponseDto.class
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Email or password is invalid."
          )
      }
  )
  Response authenticate(ApiAuthenticationRequestDto authenticationRequest);

  @POST()
  @Path("/logout")
  @Operation(
      summary = "Revoke the current user's authentication token.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "The token was successfully revoked."
          ),
          @ApiResponse(
              responseCode = "401",
              description = "The provided token is invalid."
          )
      }
  )
  @AuthenticationRequiredBinding
  Response logout();
}
