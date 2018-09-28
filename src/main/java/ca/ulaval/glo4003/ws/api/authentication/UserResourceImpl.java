package ca.ulaval.glo4003.ws.api.authentication;

import static javax.ws.rs.core.Response.Status.CREATED;

import ca.ulaval.glo4003.ws.api.InvalidInputException;
import ca.ulaval.glo4003.ws.application.user.UserCreationService;
import java.util.Set;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.core.Response;

@Resource
public class UserResourceImpl implements UserResource {

  private final UserCreationService userCreationService;

  @Inject
  public UserResourceImpl(UserCreationService userCreationService) {
    this.userCreationService = userCreationService;
  }

  @Override
  public Response createUser(UserCreationDto userCreationDto) {
    Validator validator = Validation.byDefaultProvider().configure().buildValidatorFactory().getValidator();
    Set<ConstraintViolation<UserCreationDto>> violations = validator.validate(userCreationDto);
    if (!violations.isEmpty()) {
      throw new InvalidInputException(violations);
    }
    UserDto createdUser = userCreationService.createUser(userCreationDto);
    return Response.status(CREATED).entity(createdUser).build();
  }

  private String buildErrorMessage(ConstraintViolation<?> violation) {
    return violation.getPropertyPath() + " " + violation.getMessage();
  }
}
