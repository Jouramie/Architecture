package ca.ulaval.glo4003.domain.authentication;

import java.util.Optional;
import java.util.UUID;

public interface AuthenticationTokenRepository {

  Optional<AuthenticationToken> findByUUID(UUID uuid);

  void add(AuthenticationToken token);

  void remove(String email) throws TokenNotFoundException;
}
