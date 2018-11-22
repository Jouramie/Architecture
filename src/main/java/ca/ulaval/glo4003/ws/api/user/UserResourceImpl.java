package ca.ulaval.glo4003.ws.api.user;

import static javax.ws.rs.core.Response.Status.CREATED;

import ca.ulaval.glo4003.service.user.UserDto;
import ca.ulaval.glo4003.service.user.UserService;
import ca.ulaval.glo4003.service.user.limit.LimitDto;
import ca.ulaval.glo4003.service.user.limit.LimitService;
import ca.ulaval.glo4003.ws.api.user.assemblers.ApiLimitAssembler;
import ca.ulaval.glo4003.ws.api.user.assemblers.ApiUserAssembler;
import ca.ulaval.glo4003.ws.api.user.dto.ApiLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserDto;
import ca.ulaval.glo4003.ws.api.user.dto.MoneyAmountLimitCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.StockLimitCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserCreationDto;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Resource
public class UserResourceImpl implements UserResource {

  private final UserService userService;

  private final LimitService limitService;

  private final RequestValidator requestValidator;

  private final ApiUserAssembler apiUserAssembler;

  private final ApiLimitAssembler apiUserLimitAssembler;

  @Inject
  public UserResourceImpl(UserService userService,
                          LimitService limitService,
                          RequestValidator requestValidator,
                          ApiUserAssembler apiUserAssembler,
                          ApiLimitAssembler apiUserLimitAssembler) {
    this.userService = userService;
    this.limitService = limitService;
    this.requestValidator = requestValidator;
    this.apiUserAssembler = apiUserAssembler;
    this.apiUserLimitAssembler = apiUserLimitAssembler;
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
  public Response createUser(UserCreationDto userCreationDto) {
    requestValidator.validate(userCreationDto);
    UserDto createdUser = userService.createInvestorUser(userCreationDto.email, userCreationDto.password);
    ApiUserDto apiCreatedUser = apiUserAssembler.toDto(createdUser);
    return Response.status(CREATED).entity(apiCreatedUser).build();
  }

  @Override

  public Response setUserStockLimit(String email,
                                    StockLimitCreationDto userStockLimitCreationDto) {
    requestValidator.validate(userStockLimitCreationDto);
    LimitDto limit = limitService.createStockQuantityLimit(email,
        userStockLimitCreationDto.applicationPeriod, userStockLimitCreationDto.stockQuantity);

    ApiLimitDto apiUserLimitDto = apiUserLimitAssembler.toDto(limit);
    return Response.status(CREATED).entity(apiUserLimitDto).build();
  }

  @Override
  public Response setUserMoneyAmountLimit(String email, MoneyAmountLimitCreationDto userMoneyAmountLimitCreationDto) {
    requestValidator.validate(userMoneyAmountLimitCreationDto);
    LimitDto limit = limitService.createMoneyAmountLimit(email,
        userMoneyAmountLimitCreationDto.applicationPeriod, userMoneyAmountLimitCreationDto.moneyAmount);

    ApiLimitDto apiUserLimitDto = apiUserLimitAssembler.toDto(limit);
    return Response.status(CREATED).entity(apiUserLimitDto).build();
  }

  @Override
  public Response removeUserLimit(String email) {
    limitService.removeUserLimit(email);
    return Response.noContent().build();
  }
}
