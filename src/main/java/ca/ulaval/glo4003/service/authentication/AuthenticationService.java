package ca.ulaval.glo4003.service.authentication;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenFactory;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.domain.user.authentication.TokenNotFoundException;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import ca.ulaval.glo4003.service.user.UserDoesNotExistException;
import ca.ulaval.glo4003.ws.api.authentication.dto.ApiAuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.dto.AuthenticationTokenDto;
import java.util.UUID;
import javax.inject.Inject;

@Component
public class AuthenticationService {

  private final UserRepository userRepository;
  private final AuthenticationTokenFactory tokenFactory;
  private final AuthenticationTokenRepository authenticationTokenRepository;
  private final AuthenticationResponseAssembler responseAssembler;
  private final CurrentUserSession currentUserSession;

  @Inject
  public AuthenticationService(UserRepository userRepository,
                               AuthenticationTokenFactory tokenFactory,
                               AuthenticationTokenRepository authenticationTokenRepository,
                               AuthenticationResponseAssembler responseAssembler,
                               CurrentUserSession currentUserSession) {
    this.userRepository = userRepository;
    this.tokenFactory = tokenFactory;
    this.authenticationTokenRepository = authenticationTokenRepository;
    this.responseAssembler = responseAssembler;
    this.currentUserSession = currentUserSession;
  }

  public AuthenticationResponseDto authenticate(ApiAuthenticationRequestDto authenticationRequest) {
    User user = getUserByEmail(authenticationRequest.email);
    if (user.isThisYourPassword(authenticationRequest.password)) {
      AuthenticationToken token = tokenFactory.createToken(authenticationRequest.email);
      authenticationTokenRepository.add(token);
      return responseAssembler.toDto(token);
    }
    throw new AuthenticationErrorException();
  }

  public void validateAuthentication(AuthenticationTokenDto authenticationTokenDto, UserRole requiredRole) {
    try {
      AuthenticationToken savedToken =
          authenticationTokenRepository.findByUUID(UUID.fromString(authenticationTokenDto.token));
      User currentUser = getUserByEmail(savedToken.email);

      if (!currentUser.hasRole(requiredRole)) {
        throw new InvalidTokenException();
      }

      currentUserSession.setCurrentUser(currentUser);
    } catch (TokenNotFoundException exception) {
      throw new InvalidTokenException(exception);
    }
  }

  private User getUserByEmail(String email) {
    try {
      return userRepository.find(email);
    } catch (UserNotFoundException exception) {
      throw new UserDoesNotExistException(exception);
    }
  }

  public void revokeToken() {
    try {
      User user = currentUserSession.getCurrentUser();
      authenticationTokenRepository.remove(user.getEmail());
    } catch (TokenNotFoundException exception) {
      throw new InvalidTokenException(exception);
    }
  }
}
