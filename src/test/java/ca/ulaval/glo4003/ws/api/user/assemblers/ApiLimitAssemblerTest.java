package ca.ulaval.glo4003.ws.api.user.assemblers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import ca.ulaval.glo4003.service.user.limit.LimitDto;
import ca.ulaval.glo4003.service.user.limit.MoneyAmountLimitDto;
import ca.ulaval.glo4003.service.user.limit.StockLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiMoneyAmountLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiStockLimitDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;

public class ApiLimitAssemblerTest {

  private static final LocalDateTime SOME_DATE = LocalDateTime.now();
  private static final int SOME_STOCK_QUANTITY = 4;
  private static final BigDecimal SOME_MONEY_AMOUNT = BigDecimal.valueOf(6.45);

  private final ApiLimitAssembler apiLimitAssembler = new ApiLimitAssembler();

  @Test
  public void givenStockLimit_whenAssemblingDto_thenKeepSameFieldValues() {
    StockLimitDto limit = new StockLimitDto(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);

    ApiLimitDto resultingLimit = apiLimitAssembler.toDto(limit);

    ApiStockLimitDto expectedLimit = new ApiStockLimitDto(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
    assertThat(resultingLimit).isEqualToComparingFieldByField(expectedLimit);
  }

  @Test
  public void givenMoneyAmountLimit_whenAssemblingDto_thenKeepSameFieldValues() {
    MoneyAmountLimitDto limit = new MoneyAmountLimitDto(SOME_DATE, SOME_DATE, SOME_MONEY_AMOUNT);

    ApiLimitDto resultingLimit = apiLimitAssembler.toDto(limit);

    ApiMoneyAmountLimitDto expectedLimit = new ApiMoneyAmountLimitDto(SOME_DATE, SOME_DATE, SOME_MONEY_AMOUNT);
    assertThat(resultingLimit).isEqualToComparingFieldByField(expectedLimit);
  }

  @Test
  public void givenNullLimit_whenAssemblingDto_thenReturnNull() {
    ApiLimitDto resultingLimit = apiLimitAssembler.toDto(null);

    assertThat(resultingLimit).isNull();
  }

  @Test
  public void givenAnotherTypeOfLimit_whenAssemblingDto_thenExceptionIsThrown() {
    LimitDto limit = new LimitDto(SOME_DATE, SOME_DATE) {
    };

    ThrowingCallable assembleDto = () -> apiLimitAssembler.toDto(limit);

    assertThatThrownBy(assembleDto).isInstanceOf(UnsupportedOperationException.class);
  }
}