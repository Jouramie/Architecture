package ca.ulaval.glo4003.ws.api.user;

import static javax.ws.rs.core.Response.Status.CREATED;

import ca.ulaval.glo4003.service.user.UserDto;
import ca.ulaval.glo4003.service.user.UserService;
import ca.ulaval.glo4003.ws.api.user.assemblers.ApiUserAssembler;
import ca.ulaval.glo4003.ws.api.user.dto.ApiLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserDto;
import ca.ulaval.glo4003.ws.api.user.dto.InvestorCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.MoneyAmountLimitCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.StockLimitCreationDto;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Resource
public class UserResourceImpl implements UserResource {

  private final UserService userService;

  private final RequestValidator requestValidator;

  private final ApiUserAssembler apiUserAssembler;

  @Inject
  public UserResourceImpl(UserService userService,
                          RequestValidator requestValidator,
                          ApiUserAssembler apiUserAssembler) {
    this.userService = userService;
    this.requestValidator = requestValidator;
    this.apiUserAssembler = apiUserAssembler;
  }

  @Override
  public List<ApiUserDto> getUsers() {
    List<UserDto> users = userService.getUsers();
    return apiUserAssembler.toDtoList(users);
  }

  @Override
  public ApiUserDto getUserByEmail(String email) {
    UserDto user = userService.getUser(email);
    return apiUserAssembler.toDto(user);
  }

  @Override
  public Response createInvestor(InvestorCreationDto investorCreationDto) {
    requestValidator.validate(investorCreationDto);
    UserDto createdUser = userService.createInvestorUser(investorCreationDto.email, investorCreationDto.password);
    ApiUserDto apiCreatedUser = apiUserAssembler.toDto(createdUser);
    return Response.status(CREATED).entity(apiCreatedUser).build();
  }

  @Override
  public ApiLimitDto setUserStockLimit(String email,
                                       StockLimitCreationDto stockLimitCreationDto) {

    return null;
  }

  @Override
  public ApiLimitDto setUserMoneyAmountLimit(String email,
                                             MoneyAmountLimitCreationDto moneyAmountLimitCreationDto) {

    return null;
  }

  @Override
  public Response removeUserLimit(String email) {

    return Response.noContent().build();
  }
}
