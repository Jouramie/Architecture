package ca.ulaval.glo4003.service.user.limit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.user.limit.Limit;
import ca.ulaval.glo4003.domain.user.limit.MoneyAmountLimit;
import ca.ulaval.glo4003.domain.user.limit.NullLimit;
import ca.ulaval.glo4003.domain.user.limit.StockQuantityLimit;
import java.time.LocalDateTime;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;

public class LimitAssemblerTest {

  private static final LocalDateTime SOME_DATE = LocalDateTime.now();
  private static final int SOME_STOCK_QUANTITY = 8;
  private static final MoneyAmount SOME_MONEY_AMOUNT = new MoneyAmount(1.11);

  private final LimitAssembler limitAssembler = new LimitAssembler();

  @Test
  public void givenStockLimit_whenAssemblingDto_thenKeepSameFieldValues() {
    StockQuantityLimit limit = new StockQuantityLimit(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);

    LimitDto resultingLimit = limitAssembler.toDto(limit);

    StockQuantityLimitDto expectedLimit = new StockQuantityLimitDto(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
    assertThat(resultingLimit).isEqualToComparingFieldByField(expectedLimit);
  }

  @Test
  public void givenMoneyAmountLimit_whenAssemblingDto_thenKeepSameFieldValues() {
    MoneyAmountLimit limit = new MoneyAmountLimit(SOME_DATE, SOME_DATE, SOME_MONEY_AMOUNT);

    LimitDto resultingLimit = limitAssembler.toDto(limit);

    MoneyAmountLimitDto expectedLimit = new MoneyAmountLimitDto(SOME_DATE, SOME_DATE, SOME_MONEY_AMOUNT.getAmount());
    assertThat(resultingLimit).isEqualToComparingFieldByField(expectedLimit);
  }

  @Test
  public void givenNullLimit_whenAssemblingDto_thenReturnNull() {
    NullLimit limit = new NullLimit();

    LimitDto resultingLimit = limitAssembler.toDto(limit);

    assertThat(resultingLimit).isNull();
  }

  @Test
  public void givenAnotherTypeOfLimit_whenAssemblingDto_thenExceptionIsThrown() {
    Limit limit = new Limit(SOME_DATE, SOME_DATE) {
      @Override
      public boolean doesTransactionExceedLimit(Transaction transaction) {
        return false;
      }
    };

    ThrowingCallable assembleDto = () -> limitAssembler.toDto(limit);

    assertThatThrownBy(assembleDto).isInstanceOf(UnsupportedOperationException.class);
  }
}