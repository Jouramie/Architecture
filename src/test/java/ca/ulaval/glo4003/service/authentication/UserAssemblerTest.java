package ca.ulaval.glo4003.service.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.util.UserBuilder;
import org.junit.Test;

public class UserAssemblerTest {

  private final UserAssembler assembler = new UserAssembler();

  @Test
  public void whenAssemblingToDto_thenAssembleFieldByField() {
    User someUser = new UserBuilder().buildDefault();

    UserDto createdUser = assembler.toDto(someUser);

    assertThat(createdUser).isEqualToComparingFieldByField(someUser);
  }
}
