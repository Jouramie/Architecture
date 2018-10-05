package ca.ulaval.glo4003.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.ulaval.glo4003.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.domain.user.authentication.NoTokenFoundException;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

public class InMemoryAuthenticationTokenRepositoryTest {

  private static final String SOME_EMAIL = "email";
  public static final String SOME_TOKEN = "token";

  private final AuthenticationToken token
      = new AuthenticationToken(SOME_TOKEN, SOME_EMAIL);

  private InMemoryAuthenticationTokenRepository inMemoryAuthenticationTokenRepository;

  @Before
  public void setup() {
    inMemoryAuthenticationTokenRepository = new InMemoryAuthenticationTokenRepository();
  }

  @Test
  public void whenAddingToken_thenTokenIsSaved() {
    inMemoryAuthenticationTokenRepository.add(token);

    AuthenticationToken retrievedToken =
        inMemoryAuthenticationTokenRepository.getByUUID(UUID.fromString(SOME_TOKEN));
    assertThat(retrievedToken).isEqualTo(token);
  }

  @Test
  public void givenTokenDoesNotExist_whenGettingTokenByUUID_thenTokenNotFoundExceptionIsThrown() {
    assertThatThrownBy(() -> inMemoryAuthenticationTokenRepository.getByUUID(UUID.fromString(SOME_TOKEN)))
        .isInstanceOf(NoTokenFoundException.class);
  }

  @Test
  public void whenRemovingToken_thenTokenIsRemoved() {
    inMemoryAuthenticationTokenRepository.add(token);

    inMemoryAuthenticationTokenRepository.remove(token.token);

    assertThatThrownBy(() -> inMemoryAuthenticationTokenRepository.getByUUID(token.token))
        .isInstanceOf(NoTokenFoundException.class);
  }
}
