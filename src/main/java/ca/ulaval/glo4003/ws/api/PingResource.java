package ca.ulaval.glo4003.ws.api;

import ca.ulaval.glo4003.ws.api.dto.PingDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.*;

@Path("/ping")
public class PingResource {
    @GET
    @Operation(
        summary = "Ping the API for basic smoke-test",
        description = "Return the version and the current date of the server.",
        responses = {
            @ApiResponse(
                description = "Ping value",
                content = @Content(schema = @Schema(implementation = PingDto.class))
            ),
            @ApiResponse(
                responseCode = "400", description = "Missing echo query parameter"
            )
        }
    )
    public PingDto ping(
        @Parameter(
            description = "Echo message to print back",
            required = true
        )
        @QueryParam("echo") String echo
    ) {
        if(echo == null || echo.isEmpty()) {
            throw new BadRequestException("Missing echo query parameter");
        }
        return new PingDto(echo);
    }
}
