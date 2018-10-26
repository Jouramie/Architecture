package ca.ulaval.glo4003.ws.api.ping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/ping")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PingResource {
  @GET
  @Operation(
      summary = "Ping the API for basic smoke-test.",
      description = "Return the version and the current date of the server.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Pong",
              content = @Content(
                  schema = @Schema(
                      implementation = PingDto.class
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Missing echo query parameter."
          )
      }
  )
  PingDto ping(
      @Parameter(
          description = "Message to send back.",
          required = true
      )
      @QueryParam("echo") String echo
  );
}
