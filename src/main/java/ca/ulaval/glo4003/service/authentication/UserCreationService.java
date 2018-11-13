package ca.ulaval.glo4003.service.authentication;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserFactory;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.exceptions.UserAlreadyExistsException;
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

  public UserDto createInvestorUser(String email, String password) {
    User user = userFactory.createInvestor(email, password);
    try {
      userRepository.add(user);
    } catch (UserAlreadyExistsException exception) {
      throw new InvalidUserEmailException(exception);
    }
    return userAssembler.toDto(user);
  }
}
