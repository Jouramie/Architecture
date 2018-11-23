package ca.ulaval.glo4003.service.user.limit;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import ca.ulaval.glo4003.domain.user.limit.Limit;
import ca.ulaval.glo4003.domain.user.limit.LimitFactory;
import ca.ulaval.glo4003.domain.user.limit.MoneyAmountLimit;
import ca.ulaval.glo4003.domain.user.limit.NullLimit;
import ca.ulaval.glo4003.domain.user.limit.StockQuantityLimit;
import ca.ulaval.glo4003.service.user.UserDoesNotExistException;
import java.math.BigDecimal;
import javax.inject.Inject;

@Component
public class LimitService {

  private final LimitFactory limitFactory;
  private final UserRepository userRepository;

  @Inject
  public LimitService(LimitFactory limitFactory, UserRepository userRepository) {
    this.limitFactory = limitFactory;
    this.userRepository = userRepository;
  }

  public MoneyAmountLimitDto createMoneyAmountLimit(String email, ApplicationPeriod applicationPeriod,
                                                    BigDecimal amount) {
    MoneyAmountLimit limit = limitFactory.createMoneyAmountLimit(applicationPeriod, new MoneyAmount(amount));
    setUserLimit(email, limit);
    return new MoneyAmountLimitDto(limit.start, limit.end, amount);
  }

  public StockQuantityLimitDto createStockQuantityLimit(String email, ApplicationPeriod applicationPeriod, int stockQuantity) {
    StockQuantityLimit limit = limitFactory.createStockQuantityLimit(applicationPeriod, stockQuantity);
    setUserLimit(email, limit);
    return new StockQuantityLimitDto(limit.start, limit.end, stockQuantity);
  }

  public void removeUserLimit(String email) {
    NullLimit limit = limitFactory.createNullLimit();
    setUserLimit(email, limit);
  }

  private User getUserByEmail(String email) {
    try {
      return userRepository.find(email);
    } catch (UserNotFoundException e) {
      throw new UserDoesNotExistException(e);
    }
  }

  private void setUserLimit(String email, Limit limit) {
    User user = getUserByEmail(email);
    user.setLimit(limit);
  }
}
