package ca.ulaval.glo4003.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.util.UserBuilder;
import org.junit.Test;

public class CurrentUserSessionTest {

  private static final User SOME_USER = new UserBuilder().buildDefault();
  private final CurrentUserSession userRepository = new CurrentUserSession();

  @Test
  public void whenSettingCurrentUser_thenUserCanBeRetrieved() {
    userRepository.setCurrentUser(SOME_USER);

    User retrievedUser = userRepository.getCurrentUser();
    assertThat(retrievedUser).isEqualTo(SOME_USER);
  }
}
