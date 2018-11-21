package ca.ulaval.glo4003.ws.api.user;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.ws.api.user.dto.ApiLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserDto;
import ca.ulaval.glo4003.ws.api.user.dto.MoneyAmountLimitCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.StockLimitCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserCreationDto;
import ca.ulaval.glo4003.ws.api.validation.InputErrorResponse;
import ca.ulaval.glo4003.ws.http.authentication.AuthenticationRequiredBinding;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UserResource {

  @GET
  @AuthenticationRequiredBinding(acceptedRoles = UserRole.ADMINISTRATOR)
  @Operation(
      summary = "Get all users.",
      description = "Return all users, with their information.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          implementation = ApiUserDto.class
                      )
                  )
              )
          ),
          @ApiResponse(
              responseCode = "401",
              description = "User is not logged in or not administrator."
          )
      }
  )
  List<ApiUserDto> getUsers();

  @GET
  @Path("/{email}")
  @AuthenticationRequiredBinding(acceptedRoles = UserRole.ADMINISTRATOR)
  @Operation(
      summary = "Get a user for a given email.",
      description = "Return the details of the user with the corresponding email.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  schema = @Schema(
                      implementation = ApiUserDto.class
                  )
              )
          ),
          @ApiResponse(
              responseCode = "401",
              description = "User is not logged in or not administrator."
          ),
          @ApiResponse(
              responseCode = "404",
              description = "User does not exist."
          )
      }
  )
  ApiUserDto getUserByEmail(@PathParam("email") String email);

  @POST
  @Operation(
      summary = "Create a user",
      description = "Register a new user",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "User successfully created.",
              content = @Content(
                  schema = @Schema(
                      implementation = UserCreationDto.class
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Email already exists, email and password should not be empty",
              content = @Content(
                  schema = @Schema(
                      implementation = InputErrorResponse.class
                  )
              )
          )
      }
  )
  Response createUser(@Valid UserCreationDto userCreationDto);

  @PUT
  @Path("/{email}/limit/stock")
  @AuthenticationRequiredBinding(acceptedRoles = UserRole.ADMINISTRATOR)
  @Operation(
      summary = "Set a stock per transaction limit to a user.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              content = @Content(
                  schema = @Schema(
                      implementation = ApiLimitDto.class
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid limit."
          ),
          @ApiResponse(
              responseCode = "401",
              description = "User is not logged in or not administrator."
          ),
          @ApiResponse(
              responseCode = "404",
              description = "User does not exist."
          )
      }
  )
  ApiLimitDto setUserStockLimit(
      @PathParam("email") String email,
      @Valid StockLimitCreationDto stockLimitCreationDto);

  @PUT
  @Path("/{email}/limit/money_amount")
  @AuthenticationRequiredBinding(acceptedRoles = UserRole.ADMINISTRATOR)
  @Operation(
      summary = "Set a money amount per transaction limit to a user.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              content = @Content(
                  schema = @Schema(
                      implementation = ApiLimitDto.class
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid limit."
          ),
          @ApiResponse(
              responseCode = "401",
              description = "User is not logged in or not administrator."
          ),
          @ApiResponse(
              responseCode = "404",
              description = "User does not exist."
          )
      }
  )
  ApiLimitDto setUserMoneyAmountLimit(
      @PathParam("email") String email,
      @Valid MoneyAmountLimitCreationDto moneyAmountLimitCreationDto);

  @DELETE
  @Path("/{email}/limit")
  @AuthenticationRequiredBinding(acceptedRoles = UserRole.ADMINISTRATOR)
  @Operation(
      summary = "Remove a limit from a user.",
      responses = {
          @ApiResponse(
              responseCode = "204"
          ),
          @ApiResponse(
              responseCode = "401",
              description = "User is not logged in or not administrator."
          ),
          @ApiResponse(
              responseCode = "404",
              description = "User does not exist."
          )
      }
  )
  Response removeUserLimit(@PathParam("email") String email);
}
