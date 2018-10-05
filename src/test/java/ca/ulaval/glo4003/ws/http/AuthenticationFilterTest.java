package ca.ulaval.glo4003.ws.http;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.user.authentication.TokenNotFoundException;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.service.authentication.AuthenticationService;
import ca.ulaval.glo4003.service.authentication.InvalidTokenException;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationTokenDto;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationFilterTest {

  private static final String SOME_EMAIL = "a email";

  private static final String SOME_TOKEN = "a-token";
  private ArgumentCaptor<Response> responseCaptor;
  private ArgumentCaptor<AuthenticationTokenDto> tokenDtoCaptor;
  private MultivaluedMap<String, String> headers;

  private AuthenticationFilter authenticationFilter;

  @Mock
  private ContainerRequestContext requestContext;

  @Mock
  private AuthenticationService authenticationService;

  @Before
  public void setup() {
    given(requestContext.getHeaders()).willReturn(headers);
    ServiceLocator.INSTANCE.registerInstance(AuthenticationService.class, authenticationService);
    authenticationFilter = new AuthenticationFilter();
    tokenDtoCaptor = ArgumentCaptor.forClass(AuthenticationTokenDto.class);
    responseCaptor = ArgumentCaptor.forClass(Response.class);
  }

  @Before
  public void initializeRequestContext() {
    headers = new MultivaluedHashMap<>();
    headers.putSingle("email", SOME_EMAIL);
    headers.putSingle("token", SOME_TOKEN);
    given(requestContext.getHeaders()).willReturn(headers);
  }

  @Test
  public void whenFiltering_thenTokenIsValidated() {
    authenticationFilter.filter(requestContext);

    AuthenticationTokenDto expectedTokenDto = new AuthenticationTokenDto(SOME_TOKEN);
    verify(authenticationService).validateAuthentication(tokenDtoCaptor.capture());
    assertThat(tokenDtoCaptor.getValue()).isEqualToComparingFieldByField(expectedTokenDto);
  }

  @Test
  public void givenNonExistingToken_whenFiltering_thenRequestIsAborted() {
    doThrow(TokenNotFoundException.class).when(authenticationService).validateAuthentication(any());

    authenticationFilter.filter(requestContext);

    verify(requestContext).abortWith(responseCaptor.capture());
    assertThat(responseCaptor.getValue().getStatus()).isEqualTo(UNAUTHORIZED.getStatusCode());
  }

  @Test
  public void givenInvalidToken_whenFiltering_thenRequestIsAborted() {
    doThrow(InvalidTokenException.class).when(authenticationService).validateAuthentication(any());

    authenticationFilter.filter(requestContext);

    verify(requestContext).abortWith(responseCaptor.capture());
    assertThat(responseCaptor.getValue().getStatus()).isEqualTo(UNAUTHORIZED.getStatusCode());
  }
}
