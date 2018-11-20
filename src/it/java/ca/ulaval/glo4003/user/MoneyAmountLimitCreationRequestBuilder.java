package ca.ulaval.glo4003.user;

import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import ca.ulaval.glo4003.ws.api.user.dto.MoneyAmountLimitCreationDto;

public class MoneyAmountLimitCreationRequestBuilder {
  public static final ApplicationPeriod DEFAULT_APPLICATION_PERIOD = ApplicationPeriod.DAILY;
  public static final double DEFAULT_MONEY_AMOUNT = 5;

  private double moneyAmount = DEFAULT_MONEY_AMOUNT;
  private ApplicationPeriod applicationPeriod = DEFAULT_APPLICATION_PERIOD;

  public MoneyAmountLimitCreationRequestBuilder withApplicationPeriod(ApplicationPeriod applicationPeriod) {
    this.applicationPeriod = applicationPeriod;
    return this;
  }

  public MoneyAmountLimitCreationRequestBuilder withMoneyAmount(double moneyAmount) {
    this.moneyAmount = moneyAmount;
    return this;
  }

  public MoneyAmountLimitCreationDto build() {
    return new MoneyAmountLimitCreationDto(applicationPeriod, moneyAmount);
  }
}
