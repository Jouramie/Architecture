package ca.ulaval.glo4003.ws.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.ulaval.glo4003.ws.domain.user.User;
import ca.ulaval.glo4003.ws.domain.user.UserAlreadyExistsException;
import ca.ulaval.glo4003.ws.domain.user.UserNotFoundException;
import ca.ulaval.glo4003.ws.util.UserBuilder;
import org.junit.Test;

public class InMemoryUserRepositoryTest {

  private static final User SOME_USER = new UserBuilder().buildDefault();
  private final InMemoryUserRepository inMemoryUserRepository = new InMemoryUserRepository();

  @Test
  public void whenSavingUser_thenUserIsStored() {
    inMemoryUserRepository.save(SOME_USER);

    User retrievedUser = inMemoryUserRepository.find(SOME_USER.getUsername());
    assertThat(retrievedUser).isEqualTo(SOME_USER);
  }

  @Test(expected = UserAlreadyExistsException.class)
  public void givenUserAlreadyExists_whenSavingUser_thenExceptionIsThrown() {
    inMemoryUserRepository.save(SOME_USER);

    inMemoryUserRepository.save(SOME_USER);
  }

  @Test
  public void givenUserDoesNotExist_whenGettingUser_thenUserNotFoundExceptionIsThrown() {
    assertThatThrownBy(() -> inMemoryUserRepository.find(SOME_USER.getUsername()))
        .isInstanceOf(UserNotFoundException.class);
  }
}
