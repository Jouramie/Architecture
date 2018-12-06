package ca.ulaval.glo4003.ws.api.authentication;

import ca.ulaval.glo4003.ws.api.authentication.dto.ApiAuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.dto.ApiAuthenticationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.core.Response;

public interface DocumentedAuthenticationResource {

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
  Response logout();
}
