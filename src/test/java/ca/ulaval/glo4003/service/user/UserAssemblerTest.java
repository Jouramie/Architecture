package ca.ulaval.glo4003.service.user;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserRole;
import org.junit.Test;

public class UserAssemblerTest {

  private static final String SOME_EMAIL = "1234@5678.com";
  private static final String SOME_PASSWORD = "ASDF";
  private static final UserRole SOME_ROLE = UserRole.ADMINISTRATOR;

  private final UserAssembler assembler = new UserAssembler();

  @Test
  public void whenAssemblingDto_thenKeepSameFieldValues() {
    User user = new User(SOME_EMAIL, SOME_PASSWORD, SOME_ROLE);

    UserDto createdUser = assembler.toDto(user);

    UserDto expectedUser = new UserDto(SOME_EMAIL, SOME_ROLE);
    assertThat(createdUser).isEqualToComparingFieldByField(expectedUser);
  }
}
