package ca.ulaval.glo4003.domain.user.authentication;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;

public class AuthenticationTokenFactoryTest {

  private static final String SOME_EMAIL = "email";
  private final AuthenticationTokenFactory authenticationTokenFactory = new AuthenticationTokenFactory();

  @Test
  public void whenCreatingAuthenticationToken_thenReturnANewlyGeneratedToken() {
    AuthenticationToken token = authenticationTokenFactory.createToken(SOME_EMAIL);

    assertEquals(SOME_EMAIL, token.email);
  }
}
