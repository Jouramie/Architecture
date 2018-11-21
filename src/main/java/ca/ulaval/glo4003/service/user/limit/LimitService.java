package ca.ulaval.glo4003.service.user.limit;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import ca.ulaval.glo4003.domain.user.limit.Limit;
import ca.ulaval.glo4003.domain.user.limit.LimitFactory;
import javax.inject.Inject;

@Component
public class LimitService {

  private final LimitFactory limitFactory;
  private final CurrentUserSession currentUserSession;

  @Inject
  public LimitService(LimitFactory limitFactory, CurrentUserSession currentUserSession) {
    this.limitFactory = limitFactory;
    this.currentUserSession = currentUserSession;
  }

  public void createMoneyAmountLimit(ApplicationPeriod applicationPeriod, MoneyAmount amount) {
    Limit limit = limitFactory.createMoneyAmountLimit(applicationPeriod, amount);
    User user = currentUserSession.getCurrentUser();
  }

  public void createStockQuantityLimit(ApplicationPeriod applicationPeriod, int stockQuantity) {
    Limit limit = limitFactory.createStockQuantityLimit(applicationPeriod, stockQuantity);
    User user = currentUserSession.getCurrentUser();// TODO: a terminer
  }

  public void removeUserLimit() {
    //TODO: pas fini
  }
}
