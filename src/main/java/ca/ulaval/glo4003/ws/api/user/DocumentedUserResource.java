package ca.ulaval.glo4003.ws.api.user;

import ca.ulaval.glo4003.ws.api.transaction.dto.TransactionModelDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserDto;
import ca.ulaval.glo4003.ws.api.user.dto.InvestorCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.MoneyAmountLimitCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.StockLimitCreationDto;
import ca.ulaval.glo4003.ws.api.validation.InputErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.core.Response;

public interface DocumentedUserResource {

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
              description = "Administrator is not logged in."
          )
      }
  )
  List<ApiUserDto> getUsers();

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
              description = "Administrator is not logged in."
          ),
          @ApiResponse(
              responseCode = "404",
              description = "User does not exist."
          )
      }
  )
  ApiUserDto getUserByEmail(String email);

  @Operation(
      summary = "Create an investor",
      description = "Register a new investor",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Investor successfully created.",
              content = @Content(
                  schema = @Schema(
                      implementation = InvestorCreationDto.class
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
  Response createInvestor(@Valid InvestorCreationDto investorCreationDto);

  @Operation(
      summary = "Set a stock per transaction limit to an investor.",
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
              description = "Administrator is not logged in."
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Investor does not exist."
          )
      }
  )
  Response setUserStockLimit(String email, @Valid StockLimitCreationDto stockLimitCreationDto);

  @Operation(
      summary = "Set a money amount per transaction limit to an investor.",
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
              description = "Administrator is not logged in."
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Investor does not exist."
          )
      }
  )
  Response setUserMoneyAmountLimit(String email, @Valid MoneyAmountLimitCreationDto moneyAmountLimitCreationDto);

  @Operation(
      summary = "Remove a limit from an investor.",
      responses = {
          @ApiResponse(
              responseCode = "204"
          ),
          @ApiResponse(
              responseCode = "401",
              description = "Administrator is not logged in."
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Investor does not exist."
          )
      }
  )
  Response removeUserLimit(String email);

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
              responseCode = "404",
              description = "User does not exist."
          ),
          @ApiResponse(
              responseCode = "401",
              description = "Administrator is not logged in."
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Malformed since parameter. Should be 'LAST_FIVE_DAYS' or 'LAST_THIRTY_DAYS'."
          )
      }
  )
  List<TransactionModelDto> getUserTransactions(
      @Parameter(description = "User's email") String email,
      @Parameter(description = "History since. 'LAST_FIVE_DAYS' or 'LAST_THIRTY_DAYS'") String since);
}
