package ca.ulaval.glo4003.ws.application;

import ca.ulaval.glo4003.ws.api.authentication.AuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationResponseDto;
import ca.ulaval.glo4003.ws.domain.user.User;
import ca.ulaval.glo4003.ws.domain.user.UserRepository;
import ca.ulaval.glo4003.ws.infrastructure.injection.Component;
import javax.inject.Inject;

@Component
public class AuthenticationService {

  private UserRepository userRepository;

  @Inject
  public AuthenticationService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequest) {
    User user = userRepository.find(authenticationRequest.username);

    return null;
  }
}
