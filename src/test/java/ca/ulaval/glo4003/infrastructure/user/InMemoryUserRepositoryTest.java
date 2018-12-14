package ca.ulaval.glo4003.infrastructure.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.ulaval.glo4003.domain.user.Administrator;
import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserBuilder;
import ca.ulaval.glo4003.domain.user.exception.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.exception.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.exception.WrongRoleException;
import java.util.List;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;

public class InMemoryUserRepositoryTest {


  private static final String SOME_OTHER_EMAIL = "email2@email.com";
  private static final User SOME_USER = new UserBuilder().build();
  private static final User SOME_OTHER_USER = new UserBuilder().withEmail(SOME_OTHER_EMAIL).build();

  private final InMemoryUserRepository inMemoryUserRepository = new InMemoryUserRepository();

  @Test
  public void whenAddingUser_thenUserIsStored() throws UserAlreadyExistsException, UserNotFoundException {
    inMemoryUserRepository.add(SOME_USER);

    User retrievedUser = inMemoryUserRepository.findByEmail(SOME_USER.getEmail());
    assertThat(retrievedUser).isSameAs(SOME_USER);
  }

  @Test
  public void givenUserAlreadyInRepo_whenAddingUser_thenExceptionIsThrown() throws UserAlreadyExistsException {
    inMemoryUserRepository.add(SOME_USER);

    ThrowingCallable addUser = () -> inMemoryUserRepository.add(SOME_USER);

    assertThatThrownBy(addUser).isInstanceOf(UserAlreadyExistsException.class);
  }

  @Test
  public void givenUserAlreadyInRepo_whenUpdateUser_thenUserIsStored()
      throws UserAlreadyExistsException, UserNotFoundException {
    inMemoryUserRepository.add(SOME_USER);

    User modifiedUser = new UserBuilder().withEmail(SOME_USER.getEmail()).withPassword("SOME_NEW_PASSWORD").build();
    inMemoryUserRepository.update(modifiedUser);

    User retrievedUser = inMemoryUserRepository.findByEmail(modifiedUser.getEmail());
    assertThat(retrievedUser).isSameAs(modifiedUser);
  }

  @Test
  public void givenUserNotInRepo_whenUpdateUser_thenExceptionIsThrown() {
    ThrowingCallable updateUser = () -> inMemoryUserRepository.update(SOME_OTHER_USER);

    assertThatThrownBy(updateUser).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  public void givenUserAlreadyInRepo_whenFindingUser_thenReturnTheUser()
      throws UserAlreadyExistsException, UserNotFoundException {
    inMemoryUserRepository.add(SOME_USER);

    User resultingUser = inMemoryUserRepository.findByEmail(SOME_USER.getEmail());

    assertThat(resultingUser).isSameAs(SOME_USER);
  }

  @Test
  public void givenUserDoesNotExist_whenFindingUser_thenExceptionIsThrown() {
    ThrowingCallable findUser = () -> inMemoryUserRepository.findByEmail(SOME_USER.getEmail());

    assertThatThrownBy(findUser).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  public void givenUsersAlreadyInRepo_whenFindingAllUsers_thenReturnTheUsers() throws UserAlreadyExistsException {
    inMemoryUserRepository.add(SOME_USER);
    inMemoryUserRepository.add(SOME_OTHER_USER);

    List<User> resultingUsers = inMemoryUserRepository.findAll();

    assertThat(resultingUsers).containsExactly(SOME_USER, SOME_OTHER_USER);
  }

  @Test
  public void givenInvestorAlreadyInRepo_whenFindingInvestor_thenReturnTheInvestor()
      throws UserAlreadyExistsException, UserNotFoundException, WrongRoleException {
    Investor expectedInvestor = new UserBuilder().buildInvestor();
    inMemoryUserRepository.add(expectedInvestor);

    Investor resultingInvestor = inMemoryUserRepository.findByEmail(expectedInvestor.getEmail(), Investor.class);

    assertThat(resultingInvestor).isSameAs(expectedInvestor);
  }

  @Test
  public void givenAdministratorAlreadyInRepo_whenFindingInvestor_thenExceptionIsThrown()
      throws UserAlreadyExistsException {
    Administrator administrator = new UserBuilder().buildAdministrator();
    inMemoryUserRepository.add(administrator);

    ThrowingCallable findInvestor = () -> inMemoryUserRepository.findByEmail(administrator.getEmail(), Investor.class);

    assertThatThrownBy(findInvestor).isInstanceOf(WrongRoleException.class);
  }
}
