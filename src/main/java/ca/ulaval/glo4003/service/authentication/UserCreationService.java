package ca.ulaval.glo4003.service.authentication;

import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.UserFactory;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.infrastructure.injection.Component;
import ca.ulaval.glo4003.ws.api.authentication.UserCreationDto;
import javax.inject.Inject;

@Component
public class UserCreationService {

  private final UserFactory userFactory;
  private final UserRepository userRepository;
  private final UserAssembler userAssembler;

  @Inject
  public UserCreationService(UserFactory userFactory, UserRepository userRepository,
                             UserAssembler userAssembler) {
    this.userFactory = userFactory;
    this.userRepository = userRepository;
    this.userAssembler = userAssembler;
  }

  public UserDto createUser(UserCreationDto creationRequest) {
    User user = userFactory.create(creationRequest.email, creationRequest.password,
        creationRequest.role);
    try {
      userRepository.add(user);
    } catch (UserAlreadyExistsException exception) {
      throw new InvalidUserEmailException(exception);
    }
    return userAssembler.toDto(user);
  }
}
