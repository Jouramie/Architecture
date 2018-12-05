package ca.ulaval.glo4003.ws.api.user;

import static javax.ws.rs.core.Response.Status.CREATED;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.service.user.UserDto;
import ca.ulaval.glo4003.service.user.UserService;
import ca.ulaval.glo4003.service.user.limit.LimitDto;
import ca.ulaval.glo4003.service.user.limit.LimitService;
import ca.ulaval.glo4003.ws.api.user.assemblers.ApiLimitAssembler;
import ca.ulaval.glo4003.ws.api.user.assemblers.ApiUserAssembler;
import ca.ulaval.glo4003.ws.api.user.dto.ApiLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserDto;
import ca.ulaval.glo4003.ws.api.user.dto.InvestorCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.MoneyAmountLimitCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.StockLimitCreationDto;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
import ca.ulaval.glo4003.ws.http.authentication.AuthenticationRequiredBinding;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;
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
@Resource
public class UserResource implements DocumentedUserResource {

  private final UserService userService;

  private final LimitService limitService;

  private final RequestValidator requestValidator;

  private final ApiUserAssembler apiUserAssembler;

  private final ApiLimitAssembler apiLimitAssembler;

  @Inject
  public UserResource(UserService userService,
                      LimitService limitService,
                      RequestValidator requestValidator,
                      ApiUserAssembler apiUserAssembler,
                      ApiLimitAssembler apiLimitAssembler) {
    this.userService = userService;
    this.limitService = limitService;
    this.requestValidator = requestValidator;
    this.apiUserAssembler = apiUserAssembler;
    this.apiLimitAssembler = apiLimitAssembler;
  }

  @GET
  @AuthenticationRequiredBinding(authorizedRoles = UserRole.ADMINISTRATOR)
  @Override
  public List<ApiUserDto> getUsers() {
    List<UserDto> users = userService.getUsers();
    return apiUserAssembler.toDtoList(users);
  }

  @GET
  @Path("/{email}")
  @AuthenticationRequiredBinding(authorizedRoles = UserRole.ADMINISTRATOR)
  @Override
  public ApiUserDto getUserByEmail(@PathParam("email") String email) {
    UserDto user = userService.getUser(email);
    return apiUserAssembler.toDto(user);
  }

  @POST
  @Override
  public Response createInvestor(InvestorCreationDto investorCreationDto) {
    requestValidator.validate(investorCreationDto);
    UserDto createdUser = userService.createInvestorUser(investorCreationDto.email, investorCreationDto.password);

    ApiUserDto apiCreatedUser = apiUserAssembler.toDto(createdUser);
    return Response.status(CREATED).entity(apiCreatedUser).build();
  }

  @PUT
  @Path("/{email}/limit/stock")
  @AuthenticationRequiredBinding(authorizedRoles = UserRole.ADMINISTRATOR)
  @Override
  public Response setUserStockLimit(@PathParam("email") String email,
                                    StockLimitCreationDto stockLimitCreationDto) {
    requestValidator.validate(stockLimitCreationDto);
    LimitDto limit = limitService.createStockQuantityLimit(email,
        stockLimitCreationDto.applicationPeriod, stockLimitCreationDto.stockQuantity);

    ApiLimitDto apiLimitDto = apiLimitAssembler.toDto(limit);
    return Response.status(CREATED).entity(apiLimitDto).build();
  }

  @PUT
  @Path("/{email}/limit/money_amount")
  @AuthenticationRequiredBinding(authorizedRoles = UserRole.ADMINISTRATOR)
  @Override
  public Response setUserMoneyAmountLimit(@PathParam("email") String email,
                                          MoneyAmountLimitCreationDto moneyAmountLimitCreationDto) {
    requestValidator.validate(moneyAmountLimitCreationDto);
    LimitDto limit = limitService.createMoneyAmountLimit(email,
        moneyAmountLimitCreationDto.applicationPeriod, moneyAmountLimitCreationDto.moneyAmount);

    ApiLimitDto apiLimitDto = apiLimitAssembler.toDto(limit);
    return Response.status(CREATED).entity(apiLimitDto).build();
  }

  @DELETE
  @Path("/{email}/limit")
  @AuthenticationRequiredBinding(authorizedRoles = UserRole.ADMINISTRATOR)
  @Override
  public Response removeUserLimit(@PathParam("email") String email) {
    limitService.removeUserLimit(email);
    return Response.noContent().build();
  }
}
