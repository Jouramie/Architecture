package ca.ulaval.glo4003.ws.api.user;

import static javax.ws.rs.core.Response.Status.CREATED;

import ca.ulaval.glo4003.service.user.UserDto;
import ca.ulaval.glo4003.service.user.UserService;
import ca.ulaval.glo4003.ws.api.user.assemblers.ApiUserAssembler;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserMoneyAmountLimitCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserStockLimitCreationDto;
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
    return apiUserAssembler.toDto(users);
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
  public ApiUserLimitDto setUserStockLimit(String email,
                                           UserStockLimitCreationDto userStockLimitCreationDto) {

    return null;
  }

  @Override
  public ApiUserLimitDto setUserMoneyAmountLimit(String email,
                                                 UserMoneyAmountLimitCreationDto userMoneyAmountLimitCreationDto) {

    return null;
  }

  @Override
  public Response removeUserLimit(String email) {

    return Response.noContent().build();
  }
}
