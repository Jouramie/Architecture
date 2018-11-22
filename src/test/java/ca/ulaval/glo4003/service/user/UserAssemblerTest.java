package ca.ulaval.glo4003.service.user;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.domain.user.limit.Limit;
import ca.ulaval.glo4003.domain.user.limit.StockQuantityLimit;
import ca.ulaval.glo4003.service.user.limit.LimitAssembler;
import ca.ulaval.glo4003.service.user.limit.LimitDto;
import ca.ulaval.glo4003.service.user.limit.StockLimitDto;
import ca.ulaval.glo4003.util.UserBuilder;
import java.time.LocalDateTime;
import org.junit.Test;

public class UserAssemblerTest {

  private static final String SOME_EMAIL = "1234@5678.com";
  private static final UserRole SOME_ROLE = UserRole.ADMINISTRATOR;
  private static final LocalDateTime SOME_DATE = LocalDateTime.now();
  private static final int SOME_STOCK_QUANTITY = 123;

  private final UserAssembler assembler = new UserAssembler(new LimitAssembler());

  @Test
  public void whenAssemblingDto_thenKeepSameFieldValues() {
    Limit limit = new StockQuantityLimit(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
    User user = new UserBuilder().withEmail(SOME_EMAIL).withRole(SOME_ROLE).withLimit(limit).build();

    UserDto createdUser = assembler.toDto(user);

    LimitDto expectedLimit = new StockLimitDto(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
    UserDto expectedUser = new UserDto(SOME_EMAIL, SOME_ROLE, expectedLimit);
    assertThat(createdUser).isEqualToComparingFieldByFieldRecursively(expectedUser);
  }
}
