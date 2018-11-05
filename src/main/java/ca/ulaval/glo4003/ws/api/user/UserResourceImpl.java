package ca.ulaval.glo4003.ws.api.user;

import static javax.ws.rs.core.Response.Status.CREATED;

import ca.ulaval.glo4003.service.authentication.UserCreationService;
import ca.ulaval.glo4003.service.authentication.UserDto;
import ca.ulaval.glo4003.ws.api.user.assemblers.ApiUserAssembler;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserCreationDto;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
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
  public List<ApiUserDto> getUsers() {
    return null;
  }

  @Override
  public ApiUserDto getStockByTitle(String email) {
    return null;
  }

  @Override
  public Response createUser(UserCreationDto userCreationDto) {
    requestValidator.validate(userCreationDto);
    UserDto createdUser = userCreationService.createInvestorUser(userCreationDto.email, userCreationDto.password);
    ApiUserDto apiCreatedUser = apiUserAssembler.toDto(createdUser);
    return Response.status(CREATED).entity(apiCreatedUser).build();
  }
}
