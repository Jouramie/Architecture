package ca.ulaval.glo4003.domain.portfolio;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValue;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PortfolioTest {
  private final String SOME_TITLE = "MSFT";
  private final String SOME_INVALID_TITLE = "invalid";
  private final int SOME_QUANTITY = 3;
  private final String SOME_CURRENCY_NAME = "CAD";
  private final BigDecimal SOME_RATE_TO_USD = new BigDecimal(1);
  private final Currency SOME_CURRENCY = new Currency(SOME_CURRENCY_NAME, SOME_RATE_TO_USD);
  private final MoneyAmount SOME_VALUE = new MoneyAmount(12.2, SOME_CURRENCY);
  private final StockValue SOME_STOCK_VALUE = new StockValue(SOME_VALUE, SOME_VALUE, SOME_VALUE);

  @Mock
  private Stock someStock;

  @Mock
  private StockRepository someStockRepository;

  private Portfolio portfolio;

  @Before
  public void setupPortfolio() throws StockNotFoundException {
    portfolio = new Portfolio(someStockRepository);

    willReturn(SOME_STOCK_VALUE).given(someStock).getValue();
    willReturn(SOME_CURRENCY).given(someStock).getCurrency();
    willReturn(SOME_TITLE).given(someStock).getTitle();

    willReturn(true).given(someStockRepository).doesStockExist(SOME_TITLE);
    willReturn(someStock).given(someStockRepository).findByTitle(SOME_TITLE);
    willThrow(StockNotFoundException.class).given(someStockRepository).findByTitle(SOME_INVALID_TITLE);
  }

  @Test
  public void whenAddStockToPortfolio_thenItCanBeRetrieved() throws StockNotFoundException {
    portfolio.add(SOME_TITLE, SOME_QUANTITY);

    assertThat(portfolio.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY);
  }

  @Test
  public void givenStockDoesNotExist_whenAddStockToPortfolio_thenAnExceptionIsThrown() {
    assertThatExceptionOfType(StockNotFoundException.class)
        .isThrownBy(() -> portfolio.add(SOME_INVALID_TITLE, SOME_QUANTITY));
  }

  @Test
  public void givenStockNotInPortfolio_whenGetQuantity_thenReturnZero() {
    assertThat(portfolio.getQuantity(SOME_TITLE)).isEqualTo(0);
  }

  @Test
  public void givenPortfolioNotEmpty_whenGetCurrentTotalValue_thenReturnSumOfItemValues() throws StockNotFoundException, InvalidStockInPortfolioException {
    portfolio.add(SOME_TITLE, SOME_QUANTITY);

    BigDecimal currentTotal = SOME_VALUE.getAmount().multiply(new BigDecimal(SOME_QUANTITY));
    assertThat(portfolio.getCurrentTotalValue().getAmount()).isEqualTo(currentTotal);
  }

  @Test
  public void givenPortfolioIsEmpty_whenGetCurrentTotalValue_thenReturnZero() throws InvalidStockInPortfolioException {
    BigDecimal currentTotal = new BigDecimal(0).setScale(2, RoundingMode.HALF_EVEN);
    assertThat(portfolio.getCurrentTotalValue().getAmount()).isEqualTo(currentTotal);
  }

  @Test
  public void givenPortfolioContainsInvalidStock_whenGetCurrentTotalValue_thenAnExceptionIsThrown()
      throws StockNotFoundException {
    String invalidTitle = "invalid";
    willReturn(true).given(someStockRepository).doesStockExist(invalidTitle);
    willThrow(StockNotFoundException.class).given(someStockRepository).findByTitle(invalidTitle);
    portfolio.add(invalidTitle, SOME_QUANTITY);

    assertThatExceptionOfType(InvalidStockInPortfolioException.class).isThrownBy(() -> portfolio.getCurrentTotalValue());
  }
}
