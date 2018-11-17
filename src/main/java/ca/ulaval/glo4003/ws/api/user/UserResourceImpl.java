package ca.ulaval.glo4003.ws.api.user;

import static javax.ws.rs.core.Response.Status.CREATED;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.service.authentication.UserCreationService;
import ca.ulaval.glo4003.service.authentication.UserDto;
import ca.ulaval.glo4003.ws.api.user.assemblers.ApiUserAssembler;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserMoneyAmountLimitCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserStockLimitCreationDto;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
import ca.ulaval.glo4003.ws.http.AuthenticationRequiredBinding;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Resource
public class UserResourceImpl implements UserResource {

  private final UserCreationService userCreationService;

  private final RequestValidator requestValidator;

  private final ApiUserAssembler apiUserAssembler;

  @Inject
  public UserResourceImpl(UserCreationService userCreationService,
                          RequestValidator requestValidator,
                          ApiUserAssembler apiUserAssembler) {
    this.userCreationService = userCreationService;
    this.requestValidator = requestValidator;
    this.apiUserAssembler = apiUserAssembler;
  }

  @Override
  @AuthenticationRequiredBinding(requiredRole = UserRole.ADMINISTRATOR)
  public List<ApiUserDto> getUsers() {
    return null;
  }

  @Override
  @AuthenticationRequiredBinding(requiredRole = UserRole.ADMINISTRATOR)
  public ApiUserDto getUserByEmail(String email) {
    return null;
  }

  @Override
  public Response createUser(UserCreationDto userCreationDto) {
    requestValidator.validate(userCreationDto);
    UserDto createdUser = userCreationService.createInvestorUser(userCreationDto.email, userCreationDto.password);
    ApiUserDto apiCreatedUser = apiUserAssembler.toDto(createdUser);
    return Response.status(CREATED).entity(apiCreatedUser).build();
  }

  @Override
  @AuthenticationRequiredBinding(requiredRole = UserRole.ADMINISTRATOR)
  public ApiUserLimitDto setUserStockLimit(String email,
                                           UserStockLimitCreationDto userStockLimitCreationDto) {

    return null;
  }

  @Override
  @AuthenticationRequiredBinding(requiredRole = UserRole.ADMINISTRATOR)
  public ApiUserLimitDto setUserMoneyAmountLimit(String email,
                                                 UserMoneyAmountLimitCreationDto userMoneyAmountLimitCreationDto) {

    return null;
  }

  @Override
  @AuthenticationRequiredBinding(requiredRole = UserRole.ADMINISTRATOR)
  public Response removeUserLimit(String email) {

    return Response.noContent().build();
  }
}
