package ca.ulaval.glo4003.ws.domain.user.authentication;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;

public class AuthenticationTokenFactoryTest {

  private static final String SOME_USERNAME = "username";
  private final AuthenticationTokenFactory authenticationTokenFactory = new AuthenticationTokenFactory();

  @Test
  public void whenCreatingAuthenticationToken_thenReturnANewlyGeneratedToken() {
    AuthenticationToken token = authenticationTokenFactory.createToken(SOME_USERNAME);

    assertEquals(SOME_USERNAME, token.username);
  }
}
