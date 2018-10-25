package ca.ulaval.glo4003.service.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.UserFactory;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.util.UserBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserCreationServiceTest {

  private static final String SOME_EMAIL = "email";
  private static final String SOME_PASSWORD = "password";
  private static final UserRole USER_ROLE = UserRole.INVESTOR;
  private static final UserDto USER_DTO = new UserDto(SOME_EMAIL, USER_ROLE);
  private static final UserDto SOME_CREATION_REQUEST
      = new UserDto(SOME_EMAIL, USER_ROLE);
  private static final User USER = new UserBuilder().build();

  @Mock
  private UserFactory userFactory;
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserAssembler userAssembler;

  private UserCreationService service;

  @Before
  public void setupUserCreationService() {
    service = new UserCreationService(userFactory, userRepository, userAssembler);
  }

  @Test
  public void whenCreatingUser_thenUserIsCreated() {
    service.createInvestorUser(SOME_EMAIL, SOME_PASSWORD);

    verify(userFactory).create(SOME_EMAIL, SOME_PASSWORD, USER_ROLE);
  }

  @Test
  public void whenCreatingUser_thenUserIsAdded() throws UserAlreadyExistsException {
    given(userFactory.create(SOME_EMAIL, SOME_PASSWORD, USER_ROLE)).willReturn(USER);

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
  public void givenUserAlreadyExist_whenCreatingUser_thenInvalidUserEmailExceptionIsThrown()
      throws UserAlreadyExistsException {
    doThrow(UserAlreadyExistsException.class).when(userRepository).add(any());

    assertThatThrownBy(() -> service.createInvestorUser(SOME_EMAIL, SOME_PASSWORD))
        .isInstanceOf(InvalidUserEmailException.class);
  }
}
