package ca.ulaval.glo4003.service.authentication;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.authentication.AuthenticationToken;
import ca.ulaval.glo4003.domain.authentication.AuthenticationTokenFactory;
import ca.ulaval.glo4003.domain.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.domain.authentication.TokenNotFoundException;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserBuilder;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.domain.user.exception.UserNotFoundException;
import ca.ulaval.glo4003.service.authentication.exception.AuthenticationFailedException;
import ca.ulaval.glo4003.service.authentication.exception.InvalidTokenException;
import ca.ulaval.glo4003.service.authentication.exception.UnauthorizedUserException;
import ca.ulaval.glo4003.ws.api.authentication.dto.ApiAuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.dto.AuthenticationTokenDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {


  private static final String SOME_TOKEN = "00000000-0000-0000-0000-000000000000";
  private static final String SOME_PASSWORD = UserBuilder.DEFAULT_PASSWORD;
  private static final String SOME_EMAIL = UserBuilder.DEFAULT_EMAIL;

  private static final ApiAuthenticationRequestDto AUTHENTICATION_REQUEST
      = new ApiAuthenticationRequestDto(SOME_EMAIL, SOME_PASSWORD);
  private static final ApiAuthenticationRequestDto INVALID_AUTHENTICATION_REQUEST
      = new ApiAuthenticationRequestDto(SOME_EMAIL, SOME_PASSWORD + "wrong");
  private static final AuthenticationTokenDto AUTHENTICATION_TOKEN_DTO
      = new AuthenticationTokenDto(SOME_TOKEN);
  private static final AuthenticationToken AUTHENTICATION_TOKEN
      = new AuthenticationToken(SOME_TOKEN, SOME_EMAIL);

  private static final User SOME_USER = new UserBuilder().build();
  private static final List<UserRole> SOME_USER_ROLES = singletonList(SOME_USER.getRole());

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
    authenticationService = new AuthenticationService(userRepository, tokenFactory, tokenRepository, responseAssembler,
        currentUserSession);
  }

  @Before
  public void initializeMocks() throws UserNotFoundException {
    given(currentUserSession.getCurrentUser()).willReturn(SOME_USER);
    given(userRepository.findByEmail(any())).willReturn(SOME_USER);
    given(tokenRepository.findByUUID(UUID.fromString(AUTHENTICATION_TOKEN_DTO.token)))
        .willReturn(Optional.of(AUTHENTICATION_TOKEN));
    given(tokenFactory.createToken(any())).willReturn(AUTHENTICATION_TOKEN);
  }

  @Test
  public void whenAuthenticatingUser_thenUserIsRetrievedFromRepository() throws UserNotFoundException {
    authenticationService.authenticate(AUTHENTICATION_REQUEST);

    verify(userRepository).findByEmail(AUTHENTICATION_REQUEST.email);
  }

  @Test
  public void whenAuthenticatingUser_thenTokenIsSaved() {
    authenticationService.authenticate(AUTHENTICATION_REQUEST);

    verify(tokenRepository).add(AUTHENTICATION_TOKEN);
  }

  @Test
  public void whenAuthenticatingUser_thenTokenIsReturned() {
    AuthenticationResponseDto responseDto = authenticationService.authenticate(AUTHENTICATION_REQUEST);

    assertThat(responseDto.token).isEqualTo(SOME_TOKEN);
  }

  @Test
  public void givenInvalidAuthenticationInformation_whenAuthenticatingUser_thenExceptionIsThrown() {
    ThrowingCallable authenticateUser = () -> authenticationService.authenticate(INVALID_AUTHENTICATION_REQUEST);

    assertThatThrownBy(authenticateUser).isInstanceOf(AuthenticationFailedException.class);
  }

  @Test
  public void givenUserDoesNotExist_whenAuthenticationUser_thenExceptionIsThrown() throws UserNotFoundException {
    doThrow(UserNotFoundException.class).when(userRepository).findByEmail(any());

    ThrowingCallable authenticateUser = () -> authenticationService.authenticate(INVALID_AUTHENTICATION_REQUEST);

    assertThatThrownBy(authenticateUser).isInstanceOf(AuthenticationFailedException.class);
  }

  @Test
  public void whenValidatingAuthentication_thenTokenOfUserIsRetrievedFromRepository()
      throws UnauthorizedUserException, InvalidTokenException {
    authenticationService.validateAuthentication(AUTHENTICATION_TOKEN_DTO, SOME_USER_ROLES);

    verify(tokenRepository).findByUUID(UUID.fromString(AUTHENTICATION_TOKEN_DTO.token));
  }

  @Test
  public void givenUserDoesNotExist_whenValidatingAuthentication_thenExceptionIsThrown() throws UserNotFoundException {
    doThrow(UserNotFoundException.class).when(userRepository).findByEmail(any());

    ThrowingCallable authenticateUser = () -> authenticationService
        .validateAuthentication(AUTHENTICATION_TOKEN_DTO, SOME_USER_ROLES);

    assertThatThrownBy(authenticateUser).isInstanceOf(AuthenticationFailedException.class);
  }

  @Test
  public void givenInvalidToken_whenValidatingToken_thenInvalidTokenExceptionIsThrown() {
    given(tokenRepository.findByUUID(any())).willReturn(Optional.empty());

    ThrowingCallable validateToken = () -> authenticationService.
        validateAuthentication(AUTHENTICATION_TOKEN_DTO, SOME_USER_ROLES);

    assertThatThrownBy(validateToken).isInstanceOf(InvalidTokenException.class);
  }

  @Test
  public void givenInvalidNumberFormatUUID_whenValidatingToken_thenExceptionIsThrown() {
    AuthenticationTokenDto invalidUUIDToken = new AuthenticationTokenDto("10110100-0000-0000-0000-000000000000wrong");

    ThrowingCallable validateToken = () -> authenticationService
        .validateAuthentication(invalidUUIDToken, SOME_USER_ROLES);

    assertThatThrownBy(validateToken).isInstanceOf(NumberFormatException.class);
  }

  @Test
  public void givenInvalidUUID_whenValidatingToken_thenExceptionIsThrown() {
    AuthenticationTokenDto invalidUUIDToken = new AuthenticationTokenDto("wrong");

    ThrowingCallable validateToken = () -> authenticationService
        .validateAuthentication(invalidUUIDToken, SOME_USER_ROLES);

    assertThatThrownBy(validateToken).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void givenCurrentUserDoesNotHaveRequiredRole_whenValidatingToken_thenExceptionIsThrown()
      throws UserNotFoundException {
    User currentUser = new UserBuilder().buildAdministrator();
    given(userRepository.findByEmail(any())).willReturn(currentUser);

    ThrowingCallable validateToken = () -> authenticationService
        .validateAuthentication(AUTHENTICATION_TOKEN_DTO, singletonList(UserRole.INVESTOR));

    assertThatThrownBy(validateToken).isInstanceOf(UnauthorizedUserException.class);
  }

  @Test
  public void whenValidatingToken_thenCurrentUserSet() throws UnauthorizedUserException, InvalidTokenException {
    authenticationService.validateAuthentication(AUTHENTICATION_TOKEN_DTO, SOME_USER_ROLES);

    verify(currentUserSession).setCurrentUser(SOME_USER);
  }

  @Test
  public void whenRevokingToken_thenTokenOfCurrentUserIsRevoked() {
    authenticationService.revokeToken();

    verify(currentUserSession).getCurrentUser();
  }

  @Test
  public void whenRevokingToken_thenTokenIsRemovedFromTokenRepository() throws TokenNotFoundException {
    authenticationService.revokeToken();

    verify(tokenRepository).remove(SOME_USER.getEmail());
  }

  @Test
  public void givenInvalidToken_whenRevokingToken_thenNoExceptionIsThrown()
      throws TokenNotFoundException {
    doThrow(TokenNotFoundException.class).when(tokenRepository).remove(any());

    authenticationService.revokeToken();

    verify(tokenRepository).remove(SOME_USER.getEmail());
  }
}
