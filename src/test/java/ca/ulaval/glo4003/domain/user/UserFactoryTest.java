package ca.ulaval.glo4003.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.user.limit.NullLimit;
import ca.ulaval.glo4003.util.UserBuilder;
import org.junit.Test;

public class UserFactoryTest {
  private static final String SOME_PASSWORD = "password";
  private static final String SOME_EMAIL = "email";

  private final UserFactory factory = new UserFactory();

  @Test
  public void whenCreatingInvestor_thenReturnInvestorUser() {
    User expectedUser = new UserBuilder().withEmail(SOME_EMAIL).withPassword(SOME_PASSWORD).build();

    User createdUser = factory.createInvestor(SOME_EMAIL, SOME_PASSWORD);

    assertThat(createdUser).isEqualToComparingOnlyGivenFields(expectedUser, "email", "password", "role");
  }

  @Test
  public void whenCreatingInvestor_thenCartIsEmpty() {
    User createdUser = factory.createInvestor(SOME_EMAIL, SOME_PASSWORD);

    assertThat(createdUser.getCart().isEmpty()).isTrue();
  }

  @Test
  public void whenCreatingInvestor_thenUserDoesNotOwnStock() {
    User createdUser = factory.createInvestor(SOME_EMAIL, SOME_PASSWORD);

    assertThat(createdUser.getPortfolio().getStocks().isEmpty()).isTrue();
  }

  @Test
  public void whenCreatingInvestor_thenUserIsNotLimited() {
    User createdUser = factory.createInvestor(SOME_EMAIL, SOME_PASSWORD);

    assertThat(createdUser.getLimit()).isInstanceOf(NullLimit.class);
  }

  @Test
  public void whenCreatingAdministrator_thenReturnAdministratorUser() {
    User expectedUser = new UserBuilder().withEmail(SOME_EMAIL).withPassword(SOME_PASSWORD).build();

    User createdUser = factory.createAdministrator(SOME_EMAIL, SOME_PASSWORD);

    assertThat(createdUser).isEqualToComparingOnlyGivenFields(expectedUser, "email", "password", "role");
  }
}
