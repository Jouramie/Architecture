package ca.ulaval.glo4003.ws.application.user;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.ws.api.authentication.UserDto;
import ca.ulaval.glo4003.ws.domain.user.User;
import ca.ulaval.glo4003.ws.util.UserBuilder;
import org.junit.Test;

public class UserAssemblerTest {

  private UserAssembler assembler = new UserAssembler();

  @Test
  public void whenAssemblingToDto_thenAssembleFieldByField() {
    User someUser = new UserBuilder().build();

    UserDto createdUser = assembler.toDto(someUser);

    assertThat(createdUser).isEqualToComparingFieldByField(someUser);
  }
}
