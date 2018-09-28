package ca.ulaval.glo4003.ws.application.user.authentication;

import ca.ulaval.glo4003.ws.api.authentication.AuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationResponseDto;
import ca.ulaval.glo4003.ws.domain.user.User;
import ca.ulaval.glo4003.ws.domain.user.UserRepository;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationTokenFactory;
import ca.ulaval.glo4003.ws.infrastructure.injection.Component;
import javax.inject.Inject;

@Component
public class AuthenticationService {

  private final UserRepository userRepository;
  private final AuthenticationTokenFactory tokenFactory;
  private final AuthenticationResponseAssembler responseAssembler;

  @Inject
  public AuthenticationService(UserRepository userRepository,
                               AuthenticationTokenFactory tokenFactory,
                               AuthenticationResponseAssembler responseAssembler) {
    this.userRepository = userRepository;
    this.tokenFactory = tokenFactory;
    this.responseAssembler = responseAssembler;
  }

  public AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequest) {
    User user = userRepository.find(authenticationRequest.username);
    AuthenticationToken token =
        user.authenticateByPassword(authenticationRequest.password, tokenFactory);
    return responseAssembler.toDto(token);
  }
}
