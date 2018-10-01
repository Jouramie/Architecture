package ca.ulaval.glo4003.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenFactory;
import ca.ulaval.glo4003.util.UserBuilder;
import org.junit.Before;
import org.junit.Test;

public class UserTest {

  private static final String SOME_4USERNAME = "a username";
  private static final String SOME_PASSWORD = "a password";
  private static final String WRONG_PASSWORD = SOME_PASSWORD + "wrong";
  private final AuthenticationTokenFactory tokenFactory = new AuthenticationTokenFactory();
  private User user;

  @Before
  public void initialize() {
    user = new UserBuilder().withUsername(SOME_4USERNAME).withPassword(SOME_PASSWORD).build();
  }

  @Test
  public void givenRightPassword_whenCheckingIfPasswordBelongsToUser_thenItDoes() {
    assertThat(user.isThisYourPassword(SOME_PASSWORD)).isTrue();
  }

  @Test
  public void givenWrongPassword_whenCheckingIfPasswordBelongsToUser_thenItDoesNot() {
    assertThat(user.isThisYourPassword(WRONG_PASSWORD)).isFalse();
  }
}
