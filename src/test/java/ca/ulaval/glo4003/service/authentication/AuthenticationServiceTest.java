package ca.ulaval.glo4003.service.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationErrorException;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenFactory;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.util.UserBuilder;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationResponseDto;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationTokenDto;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

  private static final String SOME_TOKEN = "token";
  private static final String SOME_PASSWORD = UserBuilder.DEFAULT_PASSWORD;
  private static final String SOME_USERNAME = UserBuilder.DEFAULT_USERNAME;
  private static final AuthenticationRequestDto AUTHENTICATION_REQUEST
      = new AuthenticationRequestDto(SOME_USERNAME, SOME_PASSWORD);
  private static final AuthenticationRequestDto INVALID_AUTHENTICATION_REQUEST
      = new AuthenticationRequestDto(SOME_USERNAME, SOME_PASSWORD + "wrong");
  private static final AuthenticationTokenDto AUTHENTICATION_TOKEN_DTO
      = new AuthenticationTokenDto(SOME_USERNAME, SOME_TOKEN);
  private static final AuthenticationTokenDto INVALID_AUTHENTICATION_TOKEN_DTO
      = new AuthenticationTokenDto(SOME_USERNAME, SOME_TOKEN + "invalid");
  private static final AuthenticationToken AUTHENTICATION_TOKEN
      = new AuthenticationToken(SOME_TOKEN, SOME_USERNAME);

  private static final User SOME_USER = new UserBuilder().buildDefault();

  private final AuthenticationResponseAssembler responseAssembler = new AuthenticationResponseAssembler();

  private final AuthenticationTokenAssembler tokenAssembler = new AuthenticationTokenAssembler();

  @Mock
  private UserRepository userRepository;

  @Mock
  private AuthenticationTokenRepository tokenRepository;

  @Mock
  private CurrentUserSession currentUserSession;

  @Mock
  private AuthenticationTokenFactory tokenFactory;

  private AuthenticationService authenticationService;

  @Before
  public void setup() {
    authenticationService = new AuthenticationService(userRepository,
        tokenAssembler, tokenFactory, tokenRepository, responseAssembler, currentUserSession);
  }

  @Before
  public void initializeMocks() {
    given(userRepository.find(any())).willReturn(SOME_USER);
    given(tokenRepository.getTokenForUser(any())).willReturn(AUTHENTICATION_TOKEN);
    given(tokenFactory.createToken(any())).willReturn(AUTHENTICATION_TOKEN);
  }

  @Test
  public void whenAuthenticatingUser_thenUserIsRetrievedFromRepository() {
    authenticationService.authenticate(AUTHENTICATION_REQUEST);

    verify(userRepository).find(AUTHENTICATION_REQUEST.username);
  }

  @Test
  public void whenAuthenticatingUser_thenTokenIsSaved() {
    authenticationService.authenticate(AUTHENTICATION_REQUEST);

    verify(tokenRepository).addToken(AUTHENTICATION_TOKEN);
  }

  @Test
  public void whenAuthenticatingUser_thenTokenIsReturned() {
    AuthenticationResponseDto responseDto
        = authenticationService.authenticate(AUTHENTICATION_REQUEST);

    assertThat(responseDto.token).isEqualTo(SOME_TOKEN);
  }

  @Test
  public void givenInvalidAuthenticationInformation_whenAuthenticatingUser_thenExceptionIsThrown() {
    ThrowableAssert.ThrowingCallable authenticateUser
        = () -> authenticationService.authenticate(INVALID_AUTHENTICATION_REQUEST);

    assertThatThrownBy(authenticateUser).isInstanceOf(AuthenticationErrorException.class);
  }

  @Test
  public void whenValidatingAuthentication_thenTokenOfUserIsRetrievedFromRepository() {
    authenticationService.validateAuthentication(AUTHENTICATION_TOKEN_DTO);

    verify(tokenRepository).getTokenForUser(AUTHENTICATION_TOKEN_DTO.username);
  }

  @Test
  public void givenInvalidToken_whenValidatingToken_thenInvalidTokenExceptionIsThrown() {
    ThrowableAssert.ThrowingCallable validateToken
        = () -> authenticationService.validateAuthentication(INVALID_AUTHENTICATION_TOKEN_DTO);

    assertThatThrownBy(validateToken).isInstanceOf(InvalidTokenException.class);
  }

  @Test
  public void whenValidatingToken_thenCurrentUserSet() {
    authenticationService.validateAuthentication(AUTHENTICATION_TOKEN_DTO);

    verify(currentUserSession).setCurrentUser(SOME_USER);
  }

  @Test
  public void whenRevokingToken_thenTokenOfCurrentUserIsRevoked() {
    given(currentUserSession.getCurrentUser()).willReturn(SOME_USER);

    authenticationService.revokeToken();

    verify(currentUserSession).getCurrentUser();
  }

  @Test
  public void whenRevokingToken_thenTokenIsRemovedFromTokenRepository() {
    given(currentUserSession.getCurrentUser()).willReturn(SOME_USER);

    authenticationService.revokeToken();

    verify(tokenRepository).removeTokenOfUser(SOME_USER.getUsername());
  }
}
