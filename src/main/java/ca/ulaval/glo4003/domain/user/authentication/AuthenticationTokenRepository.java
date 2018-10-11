package ca.ulaval.glo4003.domain.user.authentication;

import java.util.UUID;

public interface AuthenticationTokenRepository {

  AuthenticationToken getByUUID(UUID uuid) throws TokenNotFoundException;

  void add(AuthenticationToken token);

  void remove(String email) throws TokenNotFoundException;
}
