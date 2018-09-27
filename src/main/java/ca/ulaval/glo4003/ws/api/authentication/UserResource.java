package ca.ulaval.glo4003.ws.api.authentication;

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

@Path("/users")
public interface UserResource {

  @POST
  @Operation(
      summary = "Create a user",
      description = "Register a new user",
      responses = {
          @ApiResponse(
              responseCode = "201", content = @Content(schema = @Schema(implementation = UserCreationDto.class))
          ),
          @ApiResponse(
              responseCode = "400", description = "Username already exists"
          )
      }
  )
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response createUser(UserCreationDto userCreationDto);
}
