package ca.ulaval.glo4003.ws.application.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.ws.api.authentication.UserCreationDto;
import ca.ulaval.glo4003.ws.api.authentication.UserDto;
import ca.ulaval.glo4003.ws.domain.user.User;
import ca.ulaval.glo4003.ws.domain.user.UserFactory;
import ca.ulaval.glo4003.ws.domain.user.UserRepository;
import ca.ulaval.glo4003.ws.domain.user.UserRole;
import ca.ulaval.glo4003.ws.util.UserBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserCreationServiceTest {

  private static final String SOME_USERNAME = "username";
  private static final String SOME_PASSWORD = "password";
  private static final UserRole SOME_ROLE = UserRole.ADMINISTRATOR;
  private static final UserDto USER_DTO = new UserDto(SOME_USERNAME, SOME_ROLE);
  private static final UserCreationDto SOME_CREATION_REQUEST
      = new UserCreationDto(SOME_USERNAME, SOME_PASSWORD, SOME_ROLE);
  private static final User USER = new UserBuilder().buildDefault();

  @Mock
  private UserFactory userFactory;
  @Mock
  private UserAssembler userAssembler;
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserCreationService service;

  @Test
  public void whenCreatingUser_thenUserIsCreated() {

    service.createUser(SOME_CREATION_REQUEST);

    verify(userFactory).create(SOME_USERNAME, SOME_PASSWORD, SOME_ROLE);
  }

  @Test
  public void whenCreatingUser_thenUserIsSaved() {
    given(userFactory.create(SOME_USERNAME, SOME_PASSWORD, SOME_ROLE)).willReturn(USER);

    service.createUser(SOME_CREATION_REQUEST);
    verify(userRepository).save(USER);
  }

  @Test
  public void whenCreatingUser_thenReturnsUserDto() {
    given(userAssembler.toDto(any())).willReturn(USER_DTO);

    UserDto createdUser = service.createUser(SOME_CREATION_REQUEST);

    assertThat(createdUser).isEqualTo(USER_DTO);
  }
}
