package ca.ulaval.glo4003.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class UserFactoryTest {

  private static final UserRole ROLE = UserRole.ADMINISTRATOR;
  private static final String PASSWORD = "password";
  private static final String EMAIL = "email";

  private final UserFactory factory = new UserFactory(null);

  @Test
  public void whenCreatingUser_thenReturnCreatedUser() {
    User expectedUser = new User(EMAIL, PASSWORD, ROLE, null);

    User createdUser = factory.create(EMAIL, PASSWORD, ROLE);

    assertThat(createdUser).isEqualToComparingOnlyGivenFields(expectedUser, "email", "password", "role");
  }
}
