package ca.ulaval.glo4003.ws.http.authentication;

import static java.util.Collections.singletonList;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.service.authentication.AuthenticationService;
import ca.ulaval.glo4003.service.authentication.InvalidTokenException;
import ca.ulaval.glo4003.ws.api.authentication.dto.AuthenticationTokenDto;
import java.util.List;
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

  private static final String SOME_TOKEN = "a-token";

  private AuthenticationFilter authenticationFilter;

  @Mock
  private ContainerRequestContext requestContext;
  @Mock
  private AuthenticationService authenticationService;
  @Mock
  private AcceptedRoleReflectionExtractor acceptedRoleReflectionExtractor;

  @Before
  public void setup() {
    ServiceLocator.INSTANCE.registerInstance(AuthenticationService.class, authenticationService);
    ServiceLocator.INSTANCE.registerInstance(AcceptedRoleReflectionExtractor.class, acceptedRoleReflectionExtractor);

    authenticationFilter = new AuthenticationFilter();
  }

  @Before
  public void initializeRequestContext() {
    MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
    headers.putSingle("token", SOME_TOKEN);
    given(requestContext.getHeaders()).willReturn(headers);
  }

  @Test
  public void whenFiltering_thenRequiredRoleIsExtracted() {
    List<UserRole> someRoles = singletonList(UserRole.INVESTOR);
    given(acceptedRoleReflectionExtractor.extractAcceptedRoles(any())).willReturn(someRoles);

    authenticationFilter.filter(requestContext);

    verify(acceptedRoleReflectionExtractor).extractAcceptedRoles(any());
    verify(authenticationService).validateAuthentication(any(), eq(someRoles));
  }

  @Test
  public void whenFiltering_thenTokenIsValidated() {
    ArgumentCaptor<AuthenticationTokenDto> tokenDtoCaptor = ArgumentCaptor.forClass(AuthenticationTokenDto.class);
    AuthenticationTokenDto expectedTokenDto = new AuthenticationTokenDto(SOME_TOKEN);

    authenticationFilter.filter(requestContext);

    verify(authenticationService).validateAuthentication(tokenDtoCaptor.capture(), any());
    assertThat(tokenDtoCaptor.getValue()).isEqualToComparingFieldByField(expectedTokenDto);
  }

  @Test
  public void givenInvalidToken_whenFiltering_thenRequestIsAborted() {
    ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
    doThrow(InvalidTokenException.class).when(authenticationService).validateAuthentication(any(), any());

    authenticationFilter.filter(requestContext);

    verify(requestContext).abortWith(responseCaptor.capture());
    assertThat(responseCaptor.getValue().getStatus()).isEqualTo(UNAUTHORIZED.getStatusCode());
  }

  @Test
  public void givenInvalidUUID_whenFiltering_thenRequestIsAborted() {
    ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
    doThrow(IllegalArgumentException.class).when(authenticationService).validateAuthentication(any(), any());

    authenticationFilter.filter(requestContext);

    verify(requestContext).abortWith(responseCaptor.capture());
    assertThat(responseCaptor.getValue().getStatus()).isEqualTo(UNAUTHORIZED.getStatusCode());
  }
}
