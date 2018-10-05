package ca.ulaval.glo4003.service.authentication;

import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationErrorException;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenFactory;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.infrastructure.injection.Component;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationResponseDto;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationTokenDto;
import java.util.UUID;
import javax.inject.Inject;

@Component
public class AuthenticationService {

  private final UserRepository userRepository;
  private final AuthenticationTokenAssembler authenticationTokenAssembler;
  private final AuthenticationTokenFactory tokenFactory;
  private final AuthenticationTokenRepository authenticationTokenRepository;
  private final AuthenticationResponseAssembler responseAssembler;
  private final CurrentUserSession currentUserSession;

  @Inject
  public AuthenticationService(UserRepository userRepository,
                               AuthenticationTokenAssembler authenticationTokenAssembler,
                               AuthenticationTokenFactory tokenFactory,
                               AuthenticationTokenRepository authenticationTokenRepository,
                               AuthenticationResponseAssembler responseAssembler,
                               CurrentUserSession currentUserSession) {
    this.userRepository = userRepository;
    this.authenticationTokenAssembler = authenticationTokenAssembler;
    this.tokenFactory = tokenFactory;
    this.authenticationTokenRepository = authenticationTokenRepository;
    this.responseAssembler = responseAssembler;
    this.currentUserSession = currentUserSession;
  }

  public AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequest) {
    User user = userRepository.find(authenticationRequest.email);
    if (user.isThisYourPassword(authenticationRequest.password)) {
      AuthenticationToken token = tokenFactory.createToken(authenticationRequest.email);
      authenticationTokenRepository.add(token);
      return responseAssembler.toDto(token);
    }
    throw new AuthenticationErrorException();
  }

  public void validateAuthentication(AuthenticationTokenDto authenticationTokenDto) {
    AuthenticationToken savedToken =
        authenticationTokenRepository.getByUUID(UUID.fromString(authenticationTokenDto.token));
    AuthenticationToken requestToken = authenticationTokenAssembler.toModel(authenticationTokenDto);
    if (savedToken.equals(requestToken)) {
      User currentUser = userRepository.find(savedToken.email);
      currentUserSession.setCurrentUser(currentUser);
      return;
    }
    throw new InvalidTokenException();
  }

  public void revokeToken() {
    User user = currentUserSession.getCurrentUser();
    authenticationTokenRepository.remove(user.getEmail());
  }
}
