package ca.ulaval.glo4003.ws.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.ws.domain.user.authentication.NoTokenFoundException;
import org.junit.Before;
import org.junit.Test;

public class InMemoryAuthenticationTokenRepositoryTest {

  private static final String SOME_USERNAME = "username";
  
  private final AuthenticationToken token
      = new AuthenticationToken("token", SOME_USERNAME);

  private InMemoryAuthenticationTokenRepository inMemoryAuthenticationTokenRepository;

  @Before
  public void setup() {
    inMemoryAuthenticationTokenRepository = new InMemoryAuthenticationTokenRepository();
  }

  @Test
  public void whenAddingToken_thenTokenIsSaved() {
    inMemoryAuthenticationTokenRepository.addToken(token);

    AuthenticationToken retrievedToken =
        inMemoryAuthenticationTokenRepository.getTokenForUser(SOME_USERNAME);
    assertThat(retrievedToken).isEqualTo(token);
  }

  @Test
  public void givenNoTokenForAUser_whenGettingTokenOfUser_thenTokenNotFoundExceptionIsThrown() {
    assertThatThrownBy(() -> inMemoryAuthenticationTokenRepository.getTokenForUser(SOME_USERNAME))
        .isInstanceOf(NoTokenFoundException.class);
  }

  @Test
  public void whenRemovingTokenOfUser_thenTokenIsRemoved() {
    inMemoryAuthenticationTokenRepository.addToken(token);

    inMemoryAuthenticationTokenRepository.removeTokenOfUser(SOME_USERNAME);

    assertThatThrownBy(() -> inMemoryAuthenticationTokenRepository.getTokenForUser(SOME_USERNAME))
        .isInstanceOf(NoTokenFoundException.class);
  }
}