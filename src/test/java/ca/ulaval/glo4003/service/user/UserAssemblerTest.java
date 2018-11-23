package ca.ulaval.glo4003.service.user;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.domain.user.limit.Limit;
import ca.ulaval.glo4003.domain.user.limit.StockQuantityLimit;
import ca.ulaval.glo4003.service.user.limit.LimitAssembler;
import ca.ulaval.glo4003.service.user.limit.LimitDto;
import ca.ulaval.glo4003.service.user.limit.StockQuantityLimitDto;
import ca.ulaval.glo4003.util.UserBuilder;
import java.time.LocalDateTime;
import org.junit.Test;

public class UserAssemblerTest {

  private static final String SOME_EMAIL = "1234@5678.com";
  private static final LocalDateTime SOME_DATE = LocalDateTime.now();
  private static final int SOME_STOCK_QUANTITY = 123;

  private final UserAssembler assembler = new UserAssembler(new LimitAssembler());

  @Test
  public void givenInvestor_whenAssemblingDto_thenKeepSameFieldValues() {
    Limit limit = new StockQuantityLimit(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
    User user = new UserBuilder().withEmail(SOME_EMAIL).withLimit(limit).buildInvestor();

    UserDto createdUser = assembler.toDto(user);

    LimitDto expectedLimit = new StockQuantityLimitDto(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
    UserDto expectedUser = new UserDto(SOME_EMAIL, UserRole.INVESTOR, expectedLimit);
    assertThat(createdUser).isEqualToComparingFieldByFieldRecursively(expectedUser);
  }

  @Test
  public void givenAdministrator_whenAssemblingDto_thenKeepSameFieldValues() {
    User user = new UserBuilder().withEmail(SOME_EMAIL).buildAdministrator();

    UserDto createdUser = assembler.toDto(user);

    UserDto expectedUser = new UserDto(SOME_EMAIL, UserRole.ADMINISTRATOR, null);
    assertThat(createdUser).isEqualToComparingFieldByFieldRecursively(expectedUser);
  }
}
