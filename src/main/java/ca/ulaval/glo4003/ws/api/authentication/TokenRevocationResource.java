package ca.ulaval.glo4003.ws.api.authentication;

import ca.ulaval.glo4003.ws.http.AuthenticationRequiredBinding;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/revoke-token")
public interface TokenRevocationResource {

  @POST
  @Operation(
      summary = "Revoke the current user's authentication token.",
      responses = {
          @ApiResponse(
              responseCode = "200", description = "The token was successfully revoked."
          ),
          @ApiResponse(
              responseCode = "400", description = "The provided token is invalid."
          )
      }
  )
  @AuthenticationRequiredBinding
  Response revokeToken();
}
