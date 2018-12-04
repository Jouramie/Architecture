package ca.ulaval.glo4003.service.user;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserFactory;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.exceptions.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import java.util.List;
import javax.inject.Inject;

@Component
public class UserService {

  private final UserFactory userFactory;
  private final UserRepository userRepository;
  private final UserAssembler userAssembler;

  @Inject
  public UserService(UserFactory userFactory,
                     UserRepository userRepository,
                     UserAssembler userAssembler) {
    this.userFactory = userFactory;
    this.userRepository = userRepository;
    this.userAssembler = userAssembler;
  }

  public UserDto createInvestorUser(String email, String password) {
    Investor investor = userFactory.createInvestor(email, password);
    tryAddUser(investor);
    return userAssembler.toDto(investor);
  }

  public UserDto getUser(String email) {
    return userAssembler.toDto(tryFindUser(email));
  }

  private void tryAddUser(Investor investor) {
    try {
      userRepository.add(investor);
    } catch (UserAlreadyExistsException exception) {
      throw new InvalidUserEmailException(exception);
    }
  }

  private User tryFindUser(String email) {
    try {
      return userRepository.findByEmail(email);
    } catch (UserNotFoundException e) {
      throw new UserDoesNotExistException(e);
    }
  }

  public List<UserDto> getUsers() {
    List<User> users = userRepository.findAll();
    return userAssembler.toDtoList(users);
  }
}