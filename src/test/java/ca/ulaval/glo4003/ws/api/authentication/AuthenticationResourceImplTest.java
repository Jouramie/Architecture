package ca.ulaval.glo4003.ws.api.authentication;


import static javax.ws.rs.core.Response.Status.CREATED;
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

  private static final AuthenticationRequestDto AUTHENTICATION_REQUEST =
      new AuthenticationRequestDto("username", "password");

  private static final AuthenticationResponseDto AUTHENTICATION_RESPONSE
      = new AuthenticationResponseDto("TOKEN");

  @Mock
  private AuthenticationService authenticationService;

  @InjectMocks
  private AuthenticationResourceImpl authenticationResource;

  @Test
  public void whenAuthenticatingUser_thenUserIsAuthenticated() {
    authenticationResource.authenticate(AUTHENTICATION_REQUEST);

    verify(authenticationService).authenticate(AUTHENTICATION_REQUEST);
  }

  @Test
  public void givenValidAuthenticationRequest_whenAuthenticatingUser_thenResponseStatusIsOK() {
    Response response = authenticationResource.authenticate(AUTHENTICATION_REQUEST);

    assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
  }

  @Test
  public void givenValidAuthenticationRequest_whenAuthenticatingUser_thenTokenIsReturned() {
    given(authenticationService.authenticate(AUTHENTICATION_REQUEST))
        .willReturn(AUTHENTICATION_RESPONSE);

    Response response = authenticationResource.authenticate(AUTHENTICATION_REQUEST);

    assertThat(response.getEntity()).isEqualTo(AUTHENTICATION_RESPONSE);
  }
}
