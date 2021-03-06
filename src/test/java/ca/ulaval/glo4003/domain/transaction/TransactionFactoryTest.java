package ca.ulaval.glo4003.domain.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.exception.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransactionFactoryTest {
  private static final double DEFAULT_LAST_OPEN_VALUE = 40.00;
  private static final Currency DEFAULT_CURRENCY = new Currency("CAD", new BigDecimal(0.77));
  private static final MoneyAmount DEFAULT_AMOUNT = new MoneyAmount(DEFAULT_LAST_OPEN_VALUE, DEFAULT_CURRENCY);
  private static final LocalDateTime SOME_TIME = LocalDateTime.now();

  private static final String SOME_TITLE = "title";
  private static final int SOME_QUANTITY = 1;

  private TransactionFactory factory;
  @Mock
  private Clock clock;
  @Mock
  private StockRepository someStockRepository;
  @Mock
  private Stock stock;

  private StockCollection stocks;

  @Before
  public void setup() throws StockNotFoundException {
    given(clock.getCurrentTime()).willReturn(SOME_TIME);
    given(someStockRepository.exists(SOME_TITLE)).willReturn(true);
    given(someStockRepository.findByTitle(SOME_TITLE)).willReturn(stock);
    given(someStockRepository.findByTitle(SOME_TITLE).getCurrentValue()).willReturn(DEFAULT_AMOUNT);

    stocks = new StockCollection();
    stocks = stocks.add(SOME_TITLE, SOME_QUANTITY, someStockRepository);
    factory = new TransactionFactory(clock, someStockRepository);
  }

  @Test
  public void whenCreate_thenTypeIsSetToTransaction()
      throws StockNotFoundException {
    Transaction transaction = factory.createPurchase(stocks);
    Transaction expected = new TransactionBuilder().withTime(clock.getCurrentTime()).build();

    assertThat(transaction.type).isEqualTo(expected.type);
  }

  @Test
  public void whenCreate_thenLocalTimeSetToTransaction()
      throws StockNotFoundException {
    Transaction transaction = factory.createPurchase(stocks);
    Transaction expected = new TransactionBuilder().withTime(clock.getCurrentTime()).build();

    assertThat(transaction.timestamp).isEqualTo(expected.timestamp);
  }

  @Test
  public void whenCreate_thenTransactionItemsIsSetToTransaction()
      throws StockNotFoundException {
    Transaction transaction = factory.createPurchase(stocks);
    Transaction expected = new TransactionBuilder().withTime(clock.getCurrentTime()).withDefaultItems().build();

    assertThat(transaction.items).first().isEqualToComparingOnlyGivenFields(expected.items.get(0), "title", "quantity");
    assertThat(transaction.items.get(0).amount.getAmount()).isEqualTo(expected.items.get(0).amount.getAmount());
  }
}
