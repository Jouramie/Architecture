package ca.ulaval.glo4003.ws.api.authentication;

import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.ws.application.user.UserCreationService;
import ca.ulaval.glo4003.ws.domain.user.UserRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceImplTest {

  private static final UserCreationDto CREATION_REQUEST =
      new UserCreationDto("username", "password", UserRole.ADMINISTRATOR);

  @Mock
  private UserCreationService userCreationService;

  @InjectMocks
  private UserResourceImpl userResource;

  @Test
  public void whenCreatingUser_thenUserIsCreated() {
    userResource.createUser(CREATION_REQUEST);

    verify(userCreationService).createUser(CREATION_REQUEST);
  }
}
