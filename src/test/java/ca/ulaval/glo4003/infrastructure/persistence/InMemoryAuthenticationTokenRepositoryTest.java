package ca.ulaval.glo4003.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.ulaval.glo4003.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.domain.user.authentication.TokenNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;

public class InMemoryAuthenticationTokenRepositoryTest {

  private static final String SOME_EMAIL = "email";
  private static final String SOME_TOKEN = "00000000-0000-0000-0000-000000000000";

  private InMemoryAuthenticationTokenRepository authenticationTokenRepository;

  @Before
  public void setup() {
    authenticationTokenRepository = new InMemoryAuthenticationTokenRepository();
  }


  @Test
  public void whenAddingToken_thenTokenIsSaved() {
    AuthenticationToken token = new AuthenticationToken(SOME_TOKEN, SOME_EMAIL);

    authenticationTokenRepository.add(token);

    AuthenticationToken retrievedToken = authenticationTokenRepository.findByUUID(UUID.fromString(SOME_TOKEN)).get();
    assertThat(retrievedToken).isEqualTo(token);
  }

  @Test
  public void givenTokenDoesNotExist_whenFindingTokenByUUID_thenReturnEmptyOptional() {
    UUID notExistingToken = UUID.fromString(SOME_TOKEN);

    Optional<AuthenticationToken> optionalToken = authenticationTokenRepository.findByUUID(notExistingToken);

    assertThat(optionalToken).isEmpty();
  }

  @Test
  public void whenRemovingToken_thenTokenIsRemoved() throws TokenNotFoundException {
    AuthenticationToken token = new AuthenticationToken(SOME_TOKEN, SOME_EMAIL);
    authenticationTokenRepository.add(token);

    authenticationTokenRepository.remove(SOME_EMAIL);

    assertThat(authenticationTokenRepository.findByUUID(UUID.fromString(SOME_TOKEN))).isEmpty();
  }

  @Test
  public void givenNotExistingToken_whenRemovingToken_thenExceptionIsThrown() {
    ThrowingCallable removingToken = () -> authenticationTokenRepository.remove(SOME_EMAIL);

    assertThatThrownBy(removingToken).isInstanceOf(TokenNotFoundException.class);
  }
}
