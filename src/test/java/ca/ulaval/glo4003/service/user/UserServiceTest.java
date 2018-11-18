package ca.ulaval.glo4003.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserFactory;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.domain.user.exceptions.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import ca.ulaval.glo4003.service.authentication.InvalidUserEmailException;
import ca.ulaval.glo4003.service.authentication.UserAssembler;
import ca.ulaval.glo4003.service.authentication.UserDto;
import ca.ulaval.glo4003.service.authentication.UserService;
import ca.ulaval.glo4003.util.UserBuilder;
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
  private static final UserRole USER_ROLE = UserRole.INVESTOR;
  private static final UserDto USER_DTO = new UserDto(SOME_EMAIL, USER_ROLE);
  private static final User USER = new UserBuilder().build();

  @Mock
  private UserFactory userFactory;
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserAssembler userAssembler;

  private UserService service;

  @Before
  public void setupUserCreationService() {
    service = new UserService(userFactory, userRepository, userAssembler);
  }

  @Test
  public void whenCreatingUser_thenUserIsCreated() {
    service.createInvestorUser(SOME_EMAIL, SOME_PASSWORD);

    verify(userFactory).createInvestor(SOME_EMAIL, SOME_PASSWORD);
  }

  @Test
  public void whenCreatingUser_thenUserIsAddedToRepository() throws UserAlreadyExistsException {
    given(userFactory.createInvestor(SOME_EMAIL, SOME_PASSWORD)).willReturn(USER);

    service.createInvestorUser(SOME_EMAIL, SOME_PASSWORD);

    verify(userRepository).add(USER);
  }

  @Test
  public void whenCreatingUser_thenReturnsUserDto() {
    given(userAssembler.toDto(any())).willReturn(USER_DTO);

    UserDto createdUser = service.createInvestorUser(SOME_EMAIL, SOME_PASSWORD);

    assertThat(createdUser).isEqualTo(USER_DTO);
  }

  @Test
  public void givenUserAlreadyExist_whenCreatingUser_thenExceptionIsThrown() throws UserAlreadyExistsException {
    doThrow(UserAlreadyExistsException.class).when(userRepository).add(any());

    ThrowingCallable createUser = () -> service.createInvestorUser(SOME_EMAIL, SOME_PASSWORD);

    assertThatThrownBy(createUser).isInstanceOf(InvalidUserEmailException.class);
  }

  @Test
  public void whenGetUser_thenGetUserFromRepository() throws UserNotFoundException {
    service.getUser(SOME_EMAIL);

    verify(userRepository).find(SOME_EMAIL);
  }

  @Test
  public void whenGetUser_thenConvertUserToDto() throws UserNotFoundException {
    User user = new UserBuilder().build();
    given(userRepository.find(any())).willReturn(user);

    service.getUser(SOME_EMAIL);

    verify(userAssembler).toDto(user);
  }

  @Test
  public void whenGetUser_thenUserDtoIsReturned() {
    UserDto userDto = mock(UserDto.class);
    given(userAssembler.toDto(any())).willReturn(userDto);

    UserDto returnedUserDto = service.getUser(SOME_EMAIL);

    assertThat(returnedUserDto).isEqualTo(userDto);
  }

  @Test
  public void givenUserNotFound_whenGetUser_thenExceptionIsThrown() throws UserNotFoundException {
    doThrow(UserNotFoundException.class).when(userRepository).find(any());

    ThrowingCallable getUser = () -> service.getUser(SOME_EMAIL);

    assertThatThrownBy(getUser).isInstanceOf(UserDoesNotExistException.class);
  }
}
