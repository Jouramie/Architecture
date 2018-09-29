package ca.ulaval.glo4003.ws.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationTokenFactory;
import ca.ulaval.glo4003.ws.util.UserBuilder;
import org.junit.Before;
import org.junit.Test;

public class UserTest {

  private static final String USERNAME = "a username";
  private static final String PASSWORD = "a password";
  private static final String WRONG_PASSWORD = PASSWORD + "wrong";
  private final AuthenticationTokenFactory tokenFactory = new AuthenticationTokenFactory();
  private User user;

  @Before
  public void initialize() {
    user = new UserBuilder().withUsername(USERNAME).withPassword(PASSWORD).build();
  }

  @Test
  public void givenRightPassword_whenCheckingIfPasswordBelongsToUser_thenItDoes() {
    assertThat(user.isThisYourPassword(PASSWORD)).isTrue();
  }

  @Test
  public void givenWrongPassword_whenCheckingIfPasswordBelongsToUser_thenItDoes() {
    assertThat(user.isThisYourPassword(WRONG_PASSWORD)).isFalse();
  }
}
