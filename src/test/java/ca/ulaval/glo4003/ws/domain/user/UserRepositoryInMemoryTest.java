package ca.ulaval.glo4003.ws.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.ws.util.UserBuilder;
import org.junit.Test;

public class UserRepositoryInMemoryTest {

  private static final User USER = new UserBuilder().buildDefault();
  private final UserRepositoryInMemory userRepositoryInMemory = new UserRepositoryInMemory();

  @Test
  public void whenSavingUser_thenUserIsStored() {
    userRepositoryInMemory.save(USER);

    User retrievedUser = userRepositoryInMemory.find(USER.getUsername());

    assertThat(retrievedUser).isEqualTo(USER);
  }

  @Test(expected = UserAlreadyExistsException.class)
  public void givenUserAlreadyExists_whenSavingUser_thenExceptionIsThrown() {
    userRepositoryInMemory.save(USER);

    userRepositoryInMemory.save(USER);
  }
}
