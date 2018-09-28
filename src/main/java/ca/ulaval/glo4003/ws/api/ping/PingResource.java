package ca.ulaval.glo4003.ws.api.ping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/ping")
public interface PingResource {
  @GET
  @Operation(
      summary = "Ping the API for basic smoke-test",
      description = "Return the version and the current date of the server.",
      responses = {
          @ApiResponse(
              description = "Ping value",
              content = @Content(schema = @Schema(implementation = PingDto.class)),
              responseCode = "200"
          ),
          @ApiResponse(
              responseCode = "400", description = "Missing echo query parameter"
          )
      }
  )
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  PingDto ping(
      @Parameter(
          description = "Echo message to print back",
          required = true
      )
      @QueryParam("echo") String echo
  );
}
