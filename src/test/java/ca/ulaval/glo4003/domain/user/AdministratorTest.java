package ca.ulaval.glo4003.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.util.UserBuilder;
import org.junit.Test;

public class AdministratorTest {


  private final Administrator administrator = new UserBuilder().buildAdministrator();

  @Test
  public void whenGetRole_thenRoleIsInvestor() {
    UserRole role = administrator.getRole();

    assertThat(role).isSameAs(UserRole.ADMINISTRATOR);
  }
}
