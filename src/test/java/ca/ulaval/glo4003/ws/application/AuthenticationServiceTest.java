package ca.ulaval.glo4003.ws.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.ws.api.authentication.AuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationTokenDto;
import ca.ulaval.glo4003.ws.application.user.authentication.AuthenticationResponseAssembler;
import ca.ulaval.glo4003.ws.application.user.authentication.AuthenticationService;
import ca.ulaval.glo4003.ws.application.user.authentication.AuthenticationTokenAssembler;
import ca.ulaval.glo4003.ws.application.user.authentication.InvalidTokenException;
import ca.ulaval.glo4003.ws.domain.user.User;
import ca.ulaval.glo4003.ws.domain.user.UserRepository;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationTokenFactory;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationTokenRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

  private static final String TOKEN = "token";
  private static final String PASSWORD = "password";
  private static final String USERNAME = "username";
  private static final AuthenticationRequestDto AUTHENTICATION_REQUEST
      = new AuthenticationRequestDto(USERNAME, PASSWORD);
  private static final AuthenticationTokenDto AUTHENTICATION_TOKEN_DTO
      = new AuthenticationTokenDto(USERNAME, TOKEN);
  private static final AuthenticationTokenDto INVALID_AUTHENTICATION_TOKEN_DTO
      = new AuthenticationTokenDto(USERNAME, TOKEN + "invalid");
  private static final AuthenticationToken AUTHENTICATION_TOKEN
      = new AuthenticationToken(TOKEN, USERNAME);
  @Mock
  private User userMock;

  @Mock
  private UserRepository userRepository;

  @Mock
  private AuthenticationTokenRepository tokenRepository;

  @Mock
  private AuthenticationTokenFactory tokenFactory;

  @Mock
  private AuthenticationResponseAssembler responseAssembler;

  private AuthenticationTokenAssembler tokenAssembler;

  private AuthenticationService authenticationService;

  @Before
  public void initialize() {
    tokenAssembler = new AuthenticationTokenAssembler();
    authenticationService = new AuthenticationService(userRepository,
        tokenAssembler, tokenFactory, tokenRepository, responseAssembler);
    given(userRepository.find(any())).willReturn(userMock);
    given(tokenRepository.getTokenForUser(any())).willReturn(AUTHENTICATION_TOKEN);
  }

  @Test
  public void whenAuthenticatingUser_thenUserIsRetrievedFromRepository() {
    authenticationService.authenticate(AUTHENTICATION_REQUEST);

    verify(userRepository).find(AUTHENTICATION_REQUEST.username);
  }

  @Test
  public void whenAuthenticatingUser_thenUserValidatesTheProvidedIdentifyingInformation() {
    authenticationService.authenticate(AUTHENTICATION_REQUEST);

    verify(userMock).authenticateByPassword(PASSWORD, tokenFactory);
  }

  @Test
  public void whenAuthenticatingUser_thenResponseDtoIsAssembled() {
    authenticationService.authenticate(AUTHENTICATION_REQUEST);

    verify(responseAssembler).toDto(any());
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
  public void givenValidAuthenticationToken_whenValidatingToken_thenNothingHappen() {
    authenticationService.validateAuthentication(AUTHENTICATION_TOKEN_DTO);
  }
}
