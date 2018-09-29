package ca.ulaval.glo4003.ws.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.ws.domain.user.User;
import ca.ulaval.glo4003.ws.util.UserBuilder;
import org.junit.Test;

public class InMemoryCurrentUserRepositoryTest {

  private static final User USER = new UserBuilder().buildDefault();
  private final InMemoryCurrentUserRepository userRepository = new InMemoryCurrentUserRepository();

  @Test
  public void whenSettingCurrentUser_thenUserCanBeRetrieved() {
    userRepository.setCurrentUser(USER);

    User retrivedUser = userRepository.getCurrentUser();
    assertThat(retrivedUser).isEqualTo(USER);
  }
}