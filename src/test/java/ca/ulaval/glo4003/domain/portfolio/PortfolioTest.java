package ca.ulaval.glo4003.domain.portfolio;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValue;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.util.TransactionBuilder;
import ca.ulaval.glo4003.util.TransactionItemBuilder;
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
  private final int SOME_QUANTITY = 3;
  private final String SOME_CURRENCY_NAME = "CAD";
  private final BigDecimal SOME_RATE_TO_USD = new BigDecimal(12);
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
    portfolio = new Portfolio();

    given(someStock.getValue()).willReturn(SOME_STOCK_VALUE);
    given(someStock.getCurrency()).willReturn(SOME_CURRENCY);
    given(someStock.getTitle()).willReturn(SOME_TITLE);

    given(someStockRepository.exists(SOME_TITLE)).willReturn(true);
    given(someStockRepository.findByTitle(SOME_TITLE)).willReturn(someStock);
  }

  @Test
  public void whenAddStockToPortfolio_thenItCanBeRetrieved() {
    Transaction transaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_QUANTITY).build())
        .build();

    portfolio.add(transaction, someStockRepository);

    assertThat(portfolio.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY);
  }

  @Test
  public void givenStockNotInPortfolio_whenGetQuantity_thenReturnZero() {
    assertThat(portfolio.getQuantity(SOME_TITLE)).isEqualTo(0);
  }

  @Test
  public void givenPortfolioNotEmpty_whenGetCurrentTotalValue_thenReturnSumOfItemValues() throws InvalidStockInPortfolioException {
    Transaction transaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(SOME_TITLE).withQuantity(SOME_QUANTITY).build())
        .build();
    portfolio.add(transaction, someStockRepository);

    BigDecimal currentTotal = SOME_VALUE.toUsd().multiply(new BigDecimal(SOME_QUANTITY));
    assertThat(portfolio.getCurrentTotalValue(someStockRepository).getAmount()).isEqualTo(currentTotal);
  }

  @Test
  public void givenPortfolioIsEmpty_whenGetCurrentTotalValue_thenReturnZero() throws InvalidStockInPortfolioException {
    BigDecimal currentTotal = new BigDecimal(0).setScale(2, RoundingMode.HALF_EVEN);
    assertThat(portfolio.getCurrentTotalValue(someStockRepository).getAmount()).isEqualTo(currentTotal);
  }

  @Test
  public void givenPortfolioContainsInvalidStock_whenGetCurrentTotalValue_thenAnExceptionIsThrown()
      throws StockNotFoundException {
    String invalidTitle = "invalid";
    given(someStockRepository.exists(invalidTitle)).willReturn(true);
    given(someStockRepository.findByTitle(invalidTitle)).willThrow(new StockNotFoundException(invalidTitle));
    Transaction transaction = new TransactionBuilder()
        .withItem(new TransactionItemBuilder().withTitle(invalidTitle).withQuantity(SOME_QUANTITY).build())
        .build();
    portfolio.add(transaction, someStockRepository);

    assertThatExceptionOfType(InvalidStockInPortfolioException.class).isThrownBy(() -> portfolio.getCurrentTotalValue(someStockRepository));
  }
}
