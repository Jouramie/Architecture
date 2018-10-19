package ca.ulaval.glo4003.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willReturn;

import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.util.UserBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {

  private static final String SOME_EMAIL = "4email@email.com";
  private static final String SOME_PASSWORD = "a password";
  private static final String WRONG_PASSWORD = SOME_PASSWORD + "wrong";
  private static final String SOME_STOCK_TITLE = "title";
  private static final int SOME_STOCK_QUANTITY = 6;

  @Mock
  private StockRepository stockRepository;

  private User user;

  @Before
  public void initialize() {
    user = new UserBuilder().withEmail(SOME_EMAIL).withPassword(SOME_PASSWORD).build();

    willReturn(true).given(stockRepository).doesStockExist(SOME_STOCK_TITLE);
  }

  @Test
  public void givenRightPassword_whenCheckingIfPasswordBelongsToUser_thenItDoes() {
    assertThat(user.isThisYourPassword(SOME_PASSWORD)).isTrue();
  }

  @Test
  public void givenWrongPassword_whenCheckingIfPasswordBelongsToUser_thenItDoesNot() {
    assertThat(user.isThisYourPassword(WRONG_PASSWORD)).isFalse();
  }

  @Test
  public void whenCreatingUser_thenCartIsEmpty() {
    assertThat(user.getCart().isEmpty()).isTrue();
  }

  @Test
  public void whenCreatingUser_thenUserDoesNotOwnStock() {
    assertThat(user.getStocks()).isEmpty();
  }

  @Test
  public void whenAddStockToPortfolio_thenStockCanBeRetrieved() {
    user.addStockToPortfolio(SOME_STOCK_TITLE, SOME_STOCK_QUANTITY, stockRepository);

    assertThat(user.getStocks()).contains(SOME_STOCK_TITLE);
  }
}
