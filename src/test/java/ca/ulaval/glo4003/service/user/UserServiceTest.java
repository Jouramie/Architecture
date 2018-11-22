package ca.ulaval.glo4003.service.user;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserFactory;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.domain.user.exceptions.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.limit.Limit;
import ca.ulaval.glo4003.domain.user.limit.StockQuantityLimit;
import ca.ulaval.glo4003.service.user.limit.LimitAssembler;
import ca.ulaval.glo4003.service.user.limit.LimitDto;
import ca.ulaval.glo4003.service.user.limit.StockLimitDto;
import ca.ulaval.glo4003.util.UserBuilder;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  private static final String SOME_EMAIL = "email";
  private static final String SOME_PASSWORD = "password";
  private static final UserRole SOME_USER_ROLE = UserRole.INVESTOR;
  private static final User SOME_USER = new UserBuilder().build();
  private static final LocalDateTime SOME_DATE = LocalDateTime.now();
  private static final int SOME_STOCK_QUANTITY = 123;

  @Mock
  private UserFactory userFactory;
  @Mock
  private UserRepository userRepository;

  private UserService userService;

  @Before
  public void setup() {
    userService = new UserService(userFactory, userRepository, new UserAssembler(new LimitAssembler()));
  }

  @Test
  public void whenCreatingUser_thenUserIsCreated() {
    given(userFactory.createInvestor(any(), any())).willReturn(SOME_USER);

    userService.createInvestorUser(SOME_EMAIL, SOME_PASSWORD);

    verify(userFactory).createInvestor(SOME_EMAIL, SOME_PASSWORD);
  }

  @Test
  public void whenCreatingUser_thenUserIsAddedToRepository() throws UserAlreadyExistsException {
    given(userFactory.createInvestor(any(), any())).willReturn(SOME_USER);

    userService.createInvestorUser(SOME_EMAIL, SOME_PASSWORD);

    verify(userRepository).add(SOME_USER);
  }

  @Test
  public void whenCreatingUser_thenReturnsConvertedUser() {
    User user = new UserBuilder().withEmail(SOME_EMAIL).withRole(SOME_USER_ROLE).build();
    given(userFactory.createInvestor(any(), any())).willReturn(user);

    UserDto resultingUser = userService.createInvestorUser(SOME_EMAIL, SOME_PASSWORD);

    UserDto expectedUser = new UserDto(SOME_EMAIL, SOME_USER_ROLE, null);
    assertThat(resultingUser).isEqualToComparingFieldByField(expectedUser);
  }

  @Test
  public void givenUserAlreadyExist_whenCreatingUser_thenExceptionIsThrown() throws UserAlreadyExistsException {
    doThrow(UserAlreadyExistsException.class).when(userRepository).add(any());

    ThrowingCallable createUser = () -> userService.createInvestorUser(SOME_EMAIL, SOME_PASSWORD);

    assertThatThrownBy(createUser).isInstanceOf(InvalidUserEmailException.class);
  }

  @Test
  public void whenGetUser_thenGetUserFromRepository() throws UserNotFoundException {
    given(userRepository.find(any())).willReturn(SOME_USER);

    userService.getUser(SOME_EMAIL);

    verify(userRepository).find(SOME_EMAIL);
  }

  @Test
  public void whenGetUser_thenReturnConvertedUser() throws UserNotFoundException {
    Limit limit = new StockQuantityLimit(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
    User user = new UserBuilder().withEmail(SOME_EMAIL).withRole(SOME_USER_ROLE).withLimit(limit).build();
    given(userRepository.find(any())).willReturn(user);

    UserDto resultingUser = userService.getUser(SOME_EMAIL);

    LimitDto expectedLimit = new StockLimitDto(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
    UserDto expectedUser = new UserDto(SOME_EMAIL, SOME_USER_ROLE, expectedLimit);
    assertThat(resultingUser).isEqualToComparingFieldByFieldRecursively(expectedUser);
  }

  @Test
  public void givenUserNotFound_whenGetUser_thenExceptionIsThrown() throws UserNotFoundException {
    doThrow(UserNotFoundException.class).when(userRepository).find(any());

    ThrowingCallable getUser = () -> userService.getUser(SOME_EMAIL);

    assertThatThrownBy(getUser).isInstanceOf(UserDoesNotExistException.class);
  }

  @Test
  public void whenGetUsers_thenGetUsersFromRepository() {
    userService.getUsers();

    verify(userRepository).findAll();
  }

  @Test
  public void whenGetUsers_thenReturnConvertedUsers() {
    Limit limit = new StockQuantityLimit(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
    User user = new UserBuilder().withEmail(SOME_EMAIL).withRole(SOME_USER_ROLE).withLimit(limit).build();
    List<User> users = singletonList(user);
    given(userRepository.findAll()).willReturn(users);

    List<UserDto> resultingUsers = userService.getUsers();

    LimitDto expectedLimit = new StockLimitDto(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
    UserDto expectedUser = new UserDto(SOME_EMAIL, SOME_USER_ROLE, expectedLimit);
    List<UserDto> expectedUsers = singletonList(expectedUser);
    assertThat(resultingUsers).usingRecursiveFieldByFieldElementComparator().containsExactlyElementsOf(expectedUsers);
  }
}
