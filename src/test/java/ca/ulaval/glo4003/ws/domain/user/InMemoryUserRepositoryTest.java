package ca.ulaval.glo4003.ws.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.ws.util.UserBuilder;
import org.junit.Test;

public class InMemoryUserRepositoryTest {

  public static final User USER = new UserBuilder().build();
  private InMemoryUserRepository inMemoryUserRepository = new InMemoryUserRepository();

  @Test
  public void whenSavingUser_thenUserIsStored() {
    inMemoryUserRepository.save(USER);

    User gottenUser = inMemoryUserRepository.find(USER.getUsername());

    assertThat(gottenUser).isEqualTo(USER);
  }

  @Test(expected = UserAlreadyExistsException.class)
  public void givenUserAlreadyExists_whenSavingUser_thenExceptionIsThrown() {
    inMemoryUserRepository.save(USER);

    inMemoryUserRepository.save(USER);
  }
}
