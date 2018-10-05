package ca.ulaval.glo4003.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.UserNotFoundException;
import ca.ulaval.glo4003.util.UserBuilder;
import org.junit.Test;

public class InMemoryUserRepositoryTest {

  private static final User SOME_USER = new UserBuilder().buildDefault();
  private static final User SOME_OTHER_USER = new UserBuilder().buildDefault();
  private final InMemoryUserRepository inMemoryUserRepository = new InMemoryUserRepository();

  @Test
  public void whenAddingUser_thenUserIsStored() {
    inMemoryUserRepository.add(SOME_USER);

    User retrievedUser = inMemoryUserRepository.find(SOME_USER.getUsername());
    assertThat(retrievedUser).isEqualTo(SOME_USER);
  }

  @Test
  public void givenUserAlreadyExists_whenAddingUser_thenExceptionIsThrown() {
    inMemoryUserRepository.add(SOME_USER);

    assertThatThrownBy(() -> inMemoryUserRepository.add(SOME_USER))
        .isInstanceOf(UserAlreadyExistsException.class);
  }

  @Test
  public void givenUserAlreadyInRepo_whenUpdateUser_thenUserIsStored() {
    inMemoryUserRepository.add(SOME_USER);

    inMemoryUserRepository.update(SOME_OTHER_USER);

    User retrievedUser = inMemoryUserRepository.find(SOME_OTHER_USER.getUsername());
    assertThat(retrievedUser).isEqualTo(SOME_OTHER_USER);
  }

  @Test
  public void givenUserNotInRepo_whenUpdateUser_thenExceptionIsThrown() {
    assertThatThrownBy(() -> inMemoryUserRepository.update(SOME_OTHER_USER))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  public void givenUserDoesNotExist_whenGettingUser_thenUserNotFoundExceptionIsThrown() {
    assertThatThrownBy(() -> inMemoryUserRepository.find(SOME_USER.getUsername()))
        .isInstanceOf(UserNotFoundException.class);
  }
}
