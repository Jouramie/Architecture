package ca.ulaval.glo4003.ws.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.ws.api.authentication.AuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationResponseDto;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationTokenDto;
import ca.ulaval.glo4003.ws.application.user.authentication.AuthenticationResponseAssembler;
import ca.ulaval.glo4003.ws.application.user.authentication.AuthenticationService;
import ca.ulaval.glo4003.ws.application.user.authentication.AuthenticationTokenAssembler;
import ca.ulaval.glo4003.ws.application.user.authentication.InvalidTokenException;
import ca.ulaval.glo4003.ws.domain.user.CurrentUserRepository;
import ca.ulaval.glo4003.ws.domain.user.User;
import ca.ulaval.glo4003.ws.domain.user.UserRepository;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationErrorException;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationTokenFactory;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.ws.util.UserBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

  private static final String TOKEN = "token";
  private static final String PASSWORD = UserBuilder.DEFAULT_PASSWORD;
  private static final String USERNAME = UserBuilder.DEFAULT_USERNAME;
  private static final AuthenticationRequestDto AUTHENTICATION_REQUEST
      = new AuthenticationRequestDto(USERNAME, PASSWORD);
  private static final AuthenticationRequestDto INVALID_AUTHENTICATION_REQUEST
      = new AuthenticationRequestDto(USERNAME, PASSWORD + "wrong");
  private static final AuthenticationTokenDto AUTHENTICATION_TOKEN_DTO
      = new AuthenticationTokenDto(USERNAME, TOKEN);
  private static final AuthenticationTokenDto INVALID_AUTHENTICATION_TOKEN_DTO
      = new AuthenticationTokenDto(USERNAME, TOKEN + "invalid");
  private static final AuthenticationToken AUTHENTICATION_TOKEN
      = new AuthenticationToken(TOKEN, USERNAME);

  private User user;

  @Mock
  private UserRepository userRepository;

  @Mock
  private AuthenticationTokenRepository tokenRepository;

  @Mock
  private CurrentUserRepository currentUserRepository;

  @Mock
  private AuthenticationTokenFactory tokenFactory;

  private AuthenticationResponseAssembler responseAssembler;

  private AuthenticationTokenAssembler tokenAssembler;

  private AuthenticationService authenticationService;

  @Before
  public void initialize() {
    tokenAssembler = new AuthenticationTokenAssembler();
    responseAssembler = new AuthenticationResponseAssembler();
    authenticationService = new AuthenticationService(userRepository,
        tokenAssembler, tokenFactory, tokenRepository, responseAssembler, currentUserRepository);
    user = new UserBuilder().buildDefault();
  }

  @Before
  public void initializeMocks() {
    given(userRepository.find(any())).willReturn(user);
    given(tokenRepository.getTokenForUser(any())).willReturn(AUTHENTICATION_TOKEN);
    given(tokenFactory.createToken(any())).willReturn(AUTHENTICATION_TOKEN);
  }

  @Test
  public void whenAuthenticatingUser_thenUserIsRetrievedFromRepository() {
    authenticationService.authenticate(AUTHENTICATION_REQUEST);

    verify(userRepository).find(AUTHENTICATION_REQUEST.username);
  }

  @Test
  public void givenValidAuthenticationInformation_whenAuthenticatingUser_thenTokenIsSaved() {
    authenticationService.authenticate(AUTHENTICATION_REQUEST);

    verify(tokenRepository).addTokenForUser(AUTHENTICATION_TOKEN);
  }

  @Test
  public void givenValidAuthenticationInformation_whenAuthenticatingUser_thenTokenIsReturned() {
    AuthenticationResponseDto responseDto
        = authenticationService.authenticate(AUTHENTICATION_REQUEST);

    assertThat(responseDto.token).isEqualTo(TOKEN);
  }

  @Test
  public void givenInvalidAuthenticationInformation_whenAuthenticatingUser_thenExceptionIsThrown() {
    assertThatThrownBy(() -> authenticationService.authenticate(INVALID_AUTHENTICATION_REQUEST))
        .isInstanceOf(AuthenticationErrorException.class);
  }

  @Test
  public void whenValidatingAuthentication_thenTokenOfUserIsRetrievedFromRepository() {
    authenticationService.validateAuthentication(AUTHENTICATION_TOKEN_DTO);

    verify(tokenRepository).getTokenForUser(AUTHENTICATION_TOKEN_DTO.username);
  }

  @Test
  public void givenInvalidToken_whenValidatingToken_thenInvalidTokenExceptionIsThrown() {
    assertThatThrownBy(
        () -> authenticationService.validateAuthentication(INVALID_AUTHENTICATION_TOKEN_DTO))
        .isInstanceOf(InvalidTokenException.class);
  }

  @Test
  public void givenValidAuthenticationToken_whenValidatingToken_thenCurrentUserSet() {
    authenticationService.validateAuthentication(AUTHENTICATION_TOKEN_DTO);

    verify(currentUserRepository).setCurrentUser(user);
  }
}
