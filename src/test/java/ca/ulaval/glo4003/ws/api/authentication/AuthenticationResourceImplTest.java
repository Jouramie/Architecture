package ca.ulaval.glo4003.ws.api.authentication;


import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.ws.application.user.authentication.AuthenticationService;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationResourceImplTest {

  private static final AuthenticationRequestDto SOME_AUTHENTICATION_REQUEST =
      new AuthenticationRequestDto("username", "password");

  private static final AuthenticationResponseDto SOME_AUTHENTICATION_RESPONSE
      = new AuthenticationResponseDto("TOKEN");

  @Mock
  private AuthenticationService authenticationService;

  @InjectMocks
  private AuthenticationResourceImpl authenticationResource;

  @Test
  public void whenAuthenticatingUser_thenUserIsAuthenticated() {
    authenticationResource.authenticate(SOME_AUTHENTICATION_REQUEST);

    verify(authenticationService).authenticate(SOME_AUTHENTICATION_REQUEST);
  }

  @Test
  public void givenValidAuthenticationRequest_whenAuthenticatingUser_thenResponseStatusIsOK() {
    Response response = authenticationResource.authenticate(SOME_AUTHENTICATION_REQUEST);

    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
  }

  @Test
  public void givenValidAuthenticationRequest_whenAuthenticatingUser_thenTokenIsReturned() {
    given(authenticationService.authenticate(SOME_AUTHENTICATION_REQUEST))
        .willReturn(SOME_AUTHENTICATION_RESPONSE);

    Response response = authenticationResource.authenticate(SOME_AUTHENTICATION_REQUEST);

    assertThat(response.getEntity()).isEqualTo(SOME_AUTHENTICATION_RESPONSE);
  }
}
