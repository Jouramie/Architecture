package ca.ulaval.glo4003.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.util.UserBuilder;
import org.junit.Before;
import org.junit.Test;

public class UserTest {

  private static final String SOME_EMAIL = "4email@email.com";
  private static final String SOME_PASSWORD = "a password";
  private static final String WRONG_PASSWORD = SOME_PASSWORD + "wrong";

  private User user;

  @Before
  public void initialize() {
    user = new UserBuilder().withEmail(SOME_EMAIL).withPassword(SOME_PASSWORD).build();
  }

  @Test
  public void givenRightPassword_whenCheckingIfPasswordBelongsToUser_thenItDoes() {
    assertThat(user.isThisYourPassword(SOME_PASSWORD)).isTrue();
  }

  @Test
  public void givenWrongPassword_whenCheckingIfPasswordBelongsToUser_thenItDoesNot() {
    assertThat(user.isThisYourPassword(WRONG_PASSWORD)).isFalse();
  }

  @Test
  public void whenCreatingUser_thenCartIsEmpty() {
    assertThat(user.getCart().isEmpty()).isTrue();
  }
}
