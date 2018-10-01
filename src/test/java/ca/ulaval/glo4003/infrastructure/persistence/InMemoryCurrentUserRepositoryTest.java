package ca.ulaval.glo4003.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.util.UserBuilder;
import org.junit.Test;

public class InMemoryCurrentUserRepositoryTest {

  private static final User SOME_USER = new UserBuilder().buildDefault();
  private final InMemoryCurrentUserRepository userRepository = new InMemoryCurrentUserRepository();

  @Test
  public void whenSettingCurrentUser_thenUserCanBeRetrieved() {
    userRepository.setCurrentUser(SOME_USER);

    User retrivedUser = userRepository.getCurrentUser();
    assertThat(retrivedUser).isEqualTo(SOME_USER);
  }
}