package ca.ulaval.glo4003.service.user.limit;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import ca.ulaval.glo4003.domain.user.limit.LimitFactory;
import ca.ulaval.glo4003.domain.user.limit.MoneyAmountLimit;
import ca.ulaval.glo4003.domain.user.limit.NullLimit;
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
    User user;
    try {
      user = userRepository.find(email);
    } catch (UserNotFoundException e) {
      throw new UserDoesNotExistException(e);
    }
    user.setLimit(limit);
    return new MoneyAmountLimitDto(limit.start, limit.end, amount);
  }

  public StockLimitDto createStockQuantityLimit(String email, ApplicationPeriod applicationPeriod,
                                                int stockQuantity) {
    ca.ulaval.glo4003.domain.user.limit.StockQuantityLimit limit = limitFactory.createStockQuantityLimit(applicationPeriod, stockQuantity);
    User user;
    try {
      user = userRepository.find(email);
    } catch (UserNotFoundException e) {
      throw new UserDoesNotExistException(e);
    }
    user.setLimit(limit);
    return new StockLimitDto(limit.start, limit.end, stockQuantity);
  }

  public void removeUserLimit(String email) {
    User user;
    try {
      user = userRepository.find(email);
    } catch (UserNotFoundException e) {
      throw new UserDoesNotExistException(e);
    }
    user.setLimit(new NullLimit());
  }
}
