package ca.ulaval.glo4003.service.user;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.util.UserBuilder;
import org.junit.Test;

public class UserAssemblerTest {

  private static final String SOME_EMAIL = "1234@5678.com";

  private final UserAssembler assembler = new UserAssembler();

  @Test
  public void whenAssemblingDto_thenKeepSameFieldValues() {
    User user = new UserBuilder().withEmail(SOME_EMAIL).build();
    UserRole role = user.getRole();

    UserDto createdUser = assembler.toDto(user);

    UserDto expectedUser = new UserDto(SOME_EMAIL, role);
    assertThat(createdUser).isEqualToComparingFieldByField(expectedUser);
  }
}
