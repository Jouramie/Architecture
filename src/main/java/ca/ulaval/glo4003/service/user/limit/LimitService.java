package ca.ulaval.glo4003.service.user.limit;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import ca.ulaval.glo4003.domain.user.limit.Limit;
import ca.ulaval.glo4003.domain.user.limit.LimitFactory;
import ca.ulaval.glo4003.service.user.UserDoesNotExistException;
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

  public void createMoneyAmountLimit(String email, ApplicationPeriod applicationPeriod, MoneyAmount amount) {
    Limit limit = limitFactory.createMoneyAmountLimit(applicationPeriod, amount);
    User user;
    try {
      user = userRepository.find(email);
    } catch (UserNotFoundException e) {
      throw new UserDoesNotExistException(e);
    }
    user.addLimit(limit);
  }

  public void createStockQuantityLimit(String email, ApplicationPeriod applicationPeriod, int stockQuantity) {
    Limit limit = limitFactory.createStockQuantityLimit(applicationPeriod, stockQuantity);
    User user;
    try {
      user = userRepository.find(email);
    } catch (UserNotFoundException e) {
      throw new UserDoesNotExistException(e);
    }
    user.addLimit(limit);
  }

  public void removeUserLimit(String email) {
    User user;
    try {
      user = userRepository.find(email);
    } catch (UserNotFoundException e) {
      throw new UserDoesNotExistException(e);
    }
    user.removeLimit();
  }
}
