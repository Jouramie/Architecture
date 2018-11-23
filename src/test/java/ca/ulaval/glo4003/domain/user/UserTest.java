package ca.ulaval.glo4003.domain.user;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class UserTest {

  private static final String SOME_EMAIL = "4email@email.com";
  private static final String SOME_PASSWORD = "a password";
  private static final String WRONG_PASSWORD = SOME_PASSWORD + "wrong";

  private User user;

  @Before
  public void setup() {
    user = new UserFixture(SOME_EMAIL, SOME_PASSWORD);
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
  public void givenListContainingUserRole_whenHaveRoleIn_thenTrue() {
    List<UserRole> roles = singletonList(UserFixture.USER_ROLE);

    boolean haveRoleIn = user.haveRoleIn(roles);

    assertThat(haveRoleIn).isTrue();
  }

  @Test
  public void givenListNotContainingUserRole_whenHaveRoleIn_thenFalse() {
    List<UserRole> roles = emptyList();

    boolean haveRoleIn = user.haveRoleIn(roles);

    assertThat(haveRoleIn).isFalse();
  }

  private static class UserFixture extends User {

    public static final UserRole USER_ROLE = UserRole.INVESTOR;

    public UserFixture(String email, String password) {
      super(email, password);
    }

    @Override
    public UserRole getRole() {
      return USER_ROLE;
    }
  }
}
