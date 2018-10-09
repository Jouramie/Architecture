package ca.ulaval.glo4003.service.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenFactory;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.domain.user.authentication.TokenNotFoundException;
import ca.ulaval.glo4003.service.user.UserDoesNotExistException;
import ca.ulaval.glo4003.util.UserBuilder;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationResponseDto;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationTokenDto;
import java.util.UUID;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

  private static final String SOME_TOKEN = "00000000-0000-0000-0000-000000000000";
  private static final String INVALID_TOKEN = "10110100-0000-0000-0000-000000000000";
  private static final String SOME_PASSWORD = UserBuilder.DEFAULT_PASSWORD;
  private static final String SOME_EMAIL = UserBuilder.DEFAULT_EMAIL;
  private static final AuthenticationRequestDto AUTHENTICATION_REQUEST
      = new AuthenticationRequestDto(SOME_EMAIL, SOME_PASSWORD);
  private static final AuthenticationRequestDto INVALID_AUTHENTICATION_REQUEST
      = new AuthenticationRequestDto(SOME_EMAIL, SOME_PASSWORD + "wrong");
  private static final AuthenticationTokenDto AUTHENTICATION_TOKEN_DTO
      = new AuthenticationTokenDto(SOME_TOKEN);
  private static final AuthenticationTokenDto INVALID_AUTHENTICATION_TOKEN_DTO
      = new AuthenticationTokenDto(INVALID_TOKEN);
  private static final AuthenticationToken AUTHENTICATION_TOKEN
      = new AuthenticationToken(SOME_TOKEN, SOME_EMAIL);

  private static final User SOME_USER = new UserBuilder().buildDefault();

  private final AuthenticationResponseAssembler responseAssembler = new AuthenticationResponseAssembler();

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
        tokenFactory, tokenRepository, responseAssembler, currentUserSession);
  }

  @Before
  public void initializeMocks() throws Throwable {
    given(userRepository.find(any())).willReturn(SOME_USER);
    given(tokenRepository.getByUUID(UUID.fromString(AUTHENTICATION_TOKEN_DTO.token)))
        .willReturn(AUTHENTICATION_TOKEN);
    given(tokenFactory.createToken(any())).willReturn(AUTHENTICATION_TOKEN);
  }

  @Test
  public void whenAuthenticatingUser_thenUserIsRetrievedFromRepository()
      throws UserNotFoundException {
    authenticationService.authenticate(AUTHENTICATION_REQUEST);

    verify(userRepository).find(AUTHENTICATION_REQUEST.email);
  }

  @Test
  public void whenAuthenticatingUser_thenTokenIsSaved() {
    authenticationService.authenticate(AUTHENTICATION_REQUEST);

    verify(tokenRepository).add(AUTHENTICATION_TOKEN);
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
  public void givenUserDoesNotExist_whenAuthenticationUser_thenUserDoesNotExistExceptionIsThrown()
      throws UserNotFoundException{
    doThrow(UserNotFoundException.class).when(userRepository).find(any());

    ThrowableAssert.ThrowingCallable authenticateUser
        = () -> authenticationService.authenticate(INVALID_AUTHENTICATION_REQUEST);

    assertThatThrownBy(authenticateUser).isInstanceOf(UserDoesNotExistException.class);
  }

  @Test
  public void whenValidatingAuthentication_thenTokenOfUserIsRetrievedFromRepository()
      throws TokenNotFoundException{
    authenticationService.validateAuthentication(AUTHENTICATION_TOKEN_DTO);

    verify(tokenRepository).getByUUID(UUID.fromString(AUTHENTICATION_TOKEN_DTO.token));
  }

  @Test
  public void givenUserDoesNotExist_whenValidatingAuthentication_thenUserDoesNotExistExceptionIsThrown()
      throws UserNotFoundException {
    doThrow(UserNotFoundException.class).when(userRepository).find(any());

        ThrowableAssert.ThrowingCallable authenticateUser
        = () -> authenticationService.validateAuthentication(AUTHENTICATION_TOKEN_DTO);

    assertThatThrownBy(authenticateUser).isInstanceOf(UserDoesNotExistException.class);
  }

  @Test
  public void givenInvalidToken_whenValidatingToken_thenInvalidTokenExceptionIsThrown()
      throws TokenNotFoundException {
    doThrow(TokenNotFoundException.class)
        .when(tokenRepository).getByUUID(UUID.fromString(INVALID_AUTHENTICATION_TOKEN_DTO.token));

    ThrowableAssert.ThrowingCallable validateToken
        = () -> authenticationService.validateAuthentication(INVALID_AUTHENTICATION_TOKEN_DTO);

    assertThatThrownBy(validateToken).isInstanceOf(InvalidTokenException.class);
  }

  @Test
  public void givenInvalidNumberFormatUUID_whenValidatingToken_thenNumberFormatExceptionIsThrown() {
    AuthenticationTokenDto invalidUUIDToken
        = new AuthenticationTokenDto("10110100-0000-0000-0000-000000000000worng");
    ThrowableAssert.ThrowingCallable validateToken
        = () -> authenticationService.validateAuthentication(invalidUUIDToken);

    assertThatThrownBy(validateToken).isInstanceOf(NumberFormatException.class);
  }

  @Test
  public void givenInvalidUUID_whenValidatingToken_thenIllegalArgumentException() {
    AuthenticationTokenDto invalidUUIDToken
        = new AuthenticationTokenDto("wrong");
    ThrowableAssert.ThrowingCallable validateToken
        = () -> authenticationService.validateAuthentication(invalidUUIDToken);

    assertThatThrownBy(validateToken).isInstanceOf(IllegalArgumentException.class);
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
  public void whenRevokingToken_thenTokenIsRemovedFromTokenRepository()
      throws TokenNotFoundException{
    given(currentUserSession.getCurrentUser()).willReturn(SOME_USER);

    authenticationService.revokeToken();

    verify(tokenRepository).remove(SOME_USER.getEmail());
  }
}
