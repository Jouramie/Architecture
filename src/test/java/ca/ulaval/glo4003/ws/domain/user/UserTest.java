package ca.ulaval.glo4003.ws.domain.user;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationErrorException;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationTokenFactory;
import ca.ulaval.glo4003.ws.util.UserBuilder;
import org.junit.Before;
import org.junit.Test;

public class UserTest {

  private static final String USERNAME = "a username";
  private static final String PASSWORD = "a password";
  private final AuthenticationTokenFactory tokenFactory = new AuthenticationTokenFactory();
  private User user;

  @Before
  public void initialize() {
    user = new UserBuilder().withUsername(USERNAME).withPassword(PASSWORD).build();
  }

  @Test
  public void givenMatchingPassword_whenAuthenticatingByPassword_thenReturnAuthenticationToken() {
    AuthenticationToken token = user.authenticateByPassword(PASSWORD, tokenFactory);

    assertEquals(USERNAME, token.username);
  }

  @Test(expected = AuthenticationErrorException.class)
  public void givenWrongPassword_whenAuthenticatingByPassword_thenThrowAuthenticationErrorException() {
    user.authenticateByPassword("a wrong password", tokenFactory);
  }

  @Test
  public void givenPreviouslyRegisteredToken_whenCheckingIfAuthenticated_thenUserIsCorrectlyAuthenticated() {
    AuthenticationToken token = user.authenticateByPassword(PASSWORD, tokenFactory);

    boolean isAuthenticated = user.isAuthenticatedBy(token);

    assertTrue(isAuthenticated);
  }

  @Test
  public void givenUnknownAuthenticationToken_whenCheckingIfAuthenticated_thenUserIsNotAuthenticated() {
    AuthenticationToken deviouslyCraftedToken = new AuthenticationToken("invalid token", USERNAME);

    boolean isAuthenticated = user.isAuthenticatedBy(deviouslyCraftedToken);

    assertFalse(isAuthenticated);
  }
}
