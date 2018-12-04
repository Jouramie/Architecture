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
import ca.ulaval.glo4003.ws.api.user.dto.InvestorCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.MoneyAmountLimitCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.StockLimitCreationDto;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Resource
public class UserResourceImpl implements UserResource {

  private final UserService userService;

  private final LimitService limitService;

  private final ApiUserAssembler apiUserAssembler;

  private final ApiLimitAssembler apiLimitAssembler;

  @Inject
  public UserResourceImpl(UserService userService,
                          LimitService limitService,
                          ApiUserAssembler apiUserAssembler,
                          ApiLimitAssembler apiLimitAssembler) {
    this.userService = userService;
    this.limitService = limitService;
    this.apiUserAssembler = apiUserAssembler;
    this.apiLimitAssembler = apiLimitAssembler;
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
    UserDto createdUser = userService.createInvestorUser(investorCreationDto.email, investorCreationDto.password);

    ApiUserDto apiCreatedUser = apiUserAssembler.toDto(createdUser);
    return Response.status(CREATED).entity(apiCreatedUser).build();
  }

  @Override

  public Response setUserStockLimit(String email, StockLimitCreationDto stockLimitCreationDto) {
    LimitDto limit = limitService.createStockQuantityLimit(email,
        stockLimitCreationDto.applicationPeriod, stockLimitCreationDto.stockQuantity);

    ApiLimitDto apiLimitDto = apiLimitAssembler.toDto(limit);
    return Response.status(CREATED).entity(apiLimitDto).build();
  }

  @Override
  public Response setUserMoneyAmountLimit(String email, MoneyAmountLimitCreationDto moneyAmountLimitCreationDto) {
    LimitDto limit = limitService.createMoneyAmountLimit(email,
        moneyAmountLimitCreationDto.applicationPeriod, moneyAmountLimitCreationDto.moneyAmount);

    ApiLimitDto apiLimitDto = apiLimitAssembler.toDto(limit);
    return Response.status(CREATED).entity(apiLimitDto).build();
  }

  @Override
  public Response removeUserLimit(String email) {
    limitService.removeUserLimit(email);
    return Response.noContent().build();
  }
}
