package ca.ulaval.glo4003.ws.application;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.ws.api.authentication.AuthenticationRequestDto;
import ca.ulaval.glo4003.ws.application.user.authentication.AuthenticationResponseAssembler;
import ca.ulaval.glo4003.ws.application.user.authentication.AuthenticationService;
import ca.ulaval.glo4003.ws.domain.user.User;
import ca.ulaval.glo4003.ws.domain.user.UserRepository;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationTokenFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

  private static final String PASSWORD = "password";
  private static final AuthenticationRequestDto AUTHENTICATION_REQUEST
      = new AuthenticationRequestDto("username", "password");
  @Mock
  private User userMock;

  @Mock
  private UserRepository userRepository;
  @Mock
  private AuthenticationTokenFactory tokenFactoryMock;
  @Mock
  private AuthenticationResponseAssembler responseAssemblerMock;

  @InjectMocks
  private AuthenticationService authenticationService;

  @Before
  public void initialize() {
    given(userRepository.find(any())).willReturn(userMock);
  }

  @Test
  public void whenAuthenticatingUser_thenUserIsRetrievedFromRepository() {
    authenticationService.authenticate(AUTHENTICATION_REQUEST);

    verify(userRepository).find(AUTHENTICATION_REQUEST.username);
  }

  @Test
  public void whenAuthenticatingUser_thenUserValidatesTheProvidedIdentifyingInformation() {
    authenticationService.authenticate(AUTHENTICATION_REQUEST);

    verify(userMock).authenticateByPassword(PASSWORD, tokenFactoryMock);
  }

  @Test
  public void whenAuthenticatingUser_thenResponseDtoIsAssembled() {
    authenticationService.authenticate(AUTHENTICATION_REQUEST);

    verify(responseAssemblerMock).toDto(any());
  }
}
