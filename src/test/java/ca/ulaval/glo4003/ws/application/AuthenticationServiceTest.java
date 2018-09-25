package ca.ulaval.glo4003.ws.application;

import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.ws.api.authentication.AuthenticationRequestDto;
import ca.ulaval.glo4003.ws.domain.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

  private static final AuthenticationRequestDto AUTHENTICATION_REQUEST
      = new AuthenticationRequestDto("username", "password");

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private AuthenticationService authenticationService;

  @Test
  public void whenAuthenticatingUser_userIsRetrievedFromRepository() {
    authenticationService.authenticate(AUTHENTICATION_REQUEST);

    verify(userRepository).find(AUTHENTICATION_REQUEST.username);
  }
}