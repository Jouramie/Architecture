package ca.ulaval.glo4003.ws.api.user;

import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import ca.ulaval.glo4003.ws.api.user.dto.MoneyAmountLimitCreationDto;
import java.math.BigDecimal;

public class MoneyAmountLimitCreationRequestBuilder {
  public static final ApplicationPeriod DEFAULT_APPLICATION_PERIOD = ApplicationPeriod.DAILY;
  public static final BigDecimal DEFAULT_MONEY_AMOUNT = BigDecimal.valueOf(5);

  private BigDecimal moneyAmount = DEFAULT_MONEY_AMOUNT;
  private ApplicationPeriod applicationPeriod = DEFAULT_APPLICATION_PERIOD;

  public MoneyAmountLimitCreationRequestBuilder withApplicationPeriod(ApplicationPeriod applicationPeriod) {
    this.applicationPeriod = applicationPeriod;
    return this;
  }

  public MoneyAmountLimitCreationRequestBuilder withMoneyAmount(BigDecimal moneyAmount) {
    this.moneyAmount = moneyAmount;
    return this;
  }

  public MoneyAmountLimitCreationDto build() {
    return new MoneyAmountLimitCreationDto(applicationPeriod, moneyAmount);
  }
}
