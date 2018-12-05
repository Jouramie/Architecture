package ca.ulaval.glo4003.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.user.limit.NullLimit;
import org.junit.Test;

public class UserFactoryTest {
  private static final String SOME_PASSWORD = "password";
  private static final String SOME_EMAIL = "email";

  private final UserFactory factory = new UserFactory();

  @Test
  public void whenCreatingInvestor_thenReturnInvestorUser() {
    Investor expectedInvestor = new UserBuilder().withEmail(SOME_EMAIL).withPassword(SOME_PASSWORD).buildInvestor();

    Investor createdInvestor = factory.createInvestor(SOME_EMAIL, SOME_PASSWORD);

    assertThat(createdInvestor).isEqualToComparingOnlyGivenFields(expectedInvestor, "email", "password", "role");
  }

  @Test
  public void whenCreatingInvestor_thenCartIsEmpty() {
    Investor createdInvestor = factory.createInvestor(SOME_EMAIL, SOME_PASSWORD);

    assertThat(createdInvestor.getCart().isEmpty()).isTrue();
  }

  @Test
  public void whenCreatingInvestor_thenUserDoesNotOwnStock() {
    Investor createdInvestor = factory.createInvestor(SOME_EMAIL, SOME_PASSWORD);

    assertThat(createdInvestor.getPortfolio().getStocks().isEmpty()).isTrue();
  }

  @Test
  public void whenCreatingInvestor_thenUserIsNotLimited() {
    Investor createdInvestor = factory.createInvestor(SOME_EMAIL, SOME_PASSWORD);

    assertThat(createdInvestor.getLimit()).isInstanceOf(NullLimit.class);
  }

  @Test
  public void whenCreatingAdministrator_thenReturnAdministratorUser() {
    Administrator expectedAdministrator = new UserBuilder().withEmail(SOME_EMAIL)
        .withPassword(SOME_PASSWORD).buildAdministrator();

    Administrator createdAdministrator = factory.createAdministrator(SOME_EMAIL, SOME_PASSWORD);

    assertThat(createdAdministrator).isEqualToComparingOnlyGivenFields(expectedAdministrator, "email", "password", "role");
  }
}
