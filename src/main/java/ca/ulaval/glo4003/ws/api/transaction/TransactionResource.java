package ca.ulaval.glo4003.ws.api.transaction;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.ws.api.transaction.dto.TransactionModelDto;
import ca.ulaval.glo4003.ws.http.authentication.AuthenticationRequiredBinding;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
public interface TransactionResource {

  @Path("/users/{email}/transactions")
  @GET
  @AuthenticationRequiredBinding(acceptedRoles = {UserRole.ADMINISTRATOR})
  @Operation(
      summary = "Get transactions for a specific user.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          implementation = TransactionModelDto.class
                      )
                  )
              )
          ),
          @ApiResponse(
              responseCode = "401",
              description = "Administrator is not logged in."
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Malformed scope parameter. Should be '5_DAYS' or '30_DAYS'."
          )
      }
  )
  List<TransactionModelDto> getUserTransactions(
      @PathParam("email") String email,
      @QueryParam("scope")
      @Parameter(description = "History scope. '5_DAYS' or '30_DAYS'") String scope);
}
