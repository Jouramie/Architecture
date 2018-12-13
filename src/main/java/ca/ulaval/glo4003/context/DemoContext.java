package ca.ulaval.glo4003.context;

import ca.ulaval.glo4003.domain.user.UserFactory;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.domain.user.exceptions.UserAlreadyExistsException;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocatorInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoContext extends ProductionContext {

  private Logger logger = LoggerFactory.getLogger(DemoContext.class);

  public static final String DEFAULT_INVESTOR_EMAIL = "Archi.test.42@gmail.com";
  public static final String DEFAULT_INVESTOR_PASSWORD = "asdfasdf";
  public static final String DEFAULT_INVESTOR_TOKEN = "11111111-1111-1111-1111-111111111111";
  public static final String DEFAULT_ADMIN_TOKEN = "00000000-0000-0000-0000-000000000000";

  public DemoContext(JerseyApiHandlersCreator jerseyApiHandlersCreator) {
    super(jerseyApiHandlersCreator);
  }

  @Override
  protected void loadData() {
    super.loadData();
    UserRepository userRepository = serviceLocator.get(UserRepository.class);
    UserFactory userFactory = serviceLocator.get(UserFactory.class);
    try {
      userRepository.add(userFactory.createInvestor(DEFAULT_INVESTOR_EMAIL, DEFAULT_INVESTOR_PASSWORD));
    } catch (UserAlreadyExistsException exception) {
      logger.error("Test user couldn't be added");
      exception.printStackTrace();
    }

    AuthenticationTokenRepository authenticationTokenRepository = serviceLocator.get(AuthenticationTokenRepository.class);
    authenticationTokenRepository.add(new AuthenticationToken(DEFAULT_ADMIN_TOKEN, DEFAULT_ADMIN_EMAIL));
    authenticationTokenRepository.add(new AuthenticationToken(DEFAULT_INVESTOR_TOKEN, DEFAULT_INVESTOR_EMAIL));
  }
}
