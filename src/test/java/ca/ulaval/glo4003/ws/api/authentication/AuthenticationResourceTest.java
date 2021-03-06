package ca.ulaval.glo4003.ws.api.authentication;


import static javax.ws.rs.core.Response.Status.ACCEPTED;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.service.authentication.AuthenticationResponseDto;
import ca.ulaval.glo4003.service.authentication.AuthenticationService;
import ca.ulaval.glo4003.ws.api.authentication.assembler.ApiAuthenticationResponseAssembler;
import ca.ulaval.glo4003.ws.api.authentication.dto.ApiAuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.dto.ApiAuthenticationResponseDto;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationResourceTest {

  private static final ApiAuthenticationRequestDto SOME_AUTHENTICATION_REQUEST =
      new ApiAuthenticationRequestDto("email", "password");

  private static final AuthenticationResponseDto SOME_AUTHENTICATION_RESPONSE
      = new AuthenticationResponseDto("TOKEN");

  private static final ApiAuthenticationResponseDto SOME_AUTHENTICATION_API_RESPONSE = new ApiAuthenticationResponseDto("TOKEN");

  private static final ApiAuthenticationRequestDto AUTHENTICATION_REQUEST_WITHOUT_EMAIL =
      new ApiAuthenticationRequestDto(null, "password");

  private static final ApiAuthenticationRequestDto AUTHENTICATION_REQUEST_WITHOUT_PASSWORD =
      new ApiAuthenticationRequestDto("email", null);

  @Mock
  private AuthenticationService authenticationService;

  @Mock
  private ApiAuthenticationResponseAssembler apiAuthenticationResponseAssembler;

  private AuthenticationResource authenticationResource;

  @Before
  public void setup() {
    authenticationResource
        = new AuthenticationResource(authenticationService, apiAuthenticationResponseAssembler);
  }

  @Test
  public void whenAuthenticatingUser_thenUserIsAuthenticated() {
    given(authenticationService.authenticate(SOME_AUTHENTICATION_REQUEST)).willReturn(SOME_AUTHENTICATION_RESPONSE);
    authenticationResource.authenticate(SOME_AUTHENTICATION_REQUEST);

    verify(authenticationService).authenticate(SOME_AUTHENTICATION_REQUEST);
  }

  @Test
  public void givenValidAuthenticationRequest_whenAuthenticatingUser_thenResponseStatusIsAccepted() {
    given(authenticationService.authenticate(SOME_AUTHENTICATION_REQUEST)).willReturn(SOME_AUTHENTICATION_RESPONSE);
    Response response = authenticationResource.authenticate(SOME_AUTHENTICATION_REQUEST);

    assertThat(response.getStatus()).isEqualTo(ACCEPTED.getStatusCode());
  }

  @Test
  public void givenValidAuthenticationRequest_whenAuthenticatingUser_thenTokenIsReturned() {
    given(authenticationService.authenticate(SOME_AUTHENTICATION_REQUEST)).willReturn(SOME_AUTHENTICATION_RESPONSE);
    given(apiAuthenticationResponseAssembler.toDto(SOME_AUTHENTICATION_RESPONSE)).willReturn(SOME_AUTHENTICATION_API_RESPONSE);
    Response response = authenticationResource.authenticate(SOME_AUTHENTICATION_REQUEST);

    assertThat(response.getEntity()).isEqualTo(SOME_AUTHENTICATION_API_RESPONSE);
  }

  @Test
  public void whenLoggingOut_thenTokenIsRevoked() {
    authenticationResource.logout();

    verify(authenticationService).revokeToken();
  }

  @Test
  public void whenLoggingOut_thenResponseIsOk() {
    Response response = authenticationResource.logout();

    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
  }
}
