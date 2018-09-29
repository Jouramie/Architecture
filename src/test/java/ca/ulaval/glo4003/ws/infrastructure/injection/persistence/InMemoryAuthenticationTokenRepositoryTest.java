package ca.ulaval.glo4003.ws.infrastructure.injection.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.ws.domain.user.authentication.NoTokenFoundException;
import ca.ulaval.glo4003.ws.infrastructure.persistence.InMemoryAuthenticationTokenRepository;
import org.junit.Before;
import org.junit.Test;

public class InMemoryAuthenticationTokenRepositoryTest {

  private static final String USERNAME = "username";
  private final AuthenticationToken token
      = new AuthenticationToken("token", USERNAME);

  private InMemoryAuthenticationTokenRepository inMemoryAuthenticationTokenRepository;

  @Before
  public void setup() {
    inMemoryAuthenticationTokenRepository = new InMemoryAuthenticationTokenRepository();
  }

  @Test
  public void whenAddingToken_thenTokenIsSaved() {
    inMemoryAuthenticationTokenRepository.addTokenForUser(token);

    AuthenticationToken retrievedToken =
        inMemoryAuthenticationTokenRepository.getTokenForUser(USERNAME);
    assertThat(retrievedToken).isEqualTo(token);
  }

  @Test
  public void givenNoTokenForAUser_whenGettingTokenOfUser_thenTokenNotFoundExceptionIsThrown() {
    assertThatThrownBy(() -> inMemoryAuthenticationTokenRepository.getTokenForUser(USERNAME))
        .isInstanceOf(NoTokenFoundException.class);
  }
}