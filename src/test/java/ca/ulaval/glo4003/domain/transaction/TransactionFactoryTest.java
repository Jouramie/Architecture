package ca.ulaval.glo4003.domain.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValue;
import ca.ulaval.glo4003.util.TransactionBuilder;
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

  private static final int SOME_QUANTITY = 1;
  private static final String SOME_TITLE = "title";

  private TransactionFactory factory;
  @Mock
  private Clock clock;
  @Mock
  private StockRepository stockRepository;
  @Mock
  private Stock stock;
  @Mock
  private StockValue stockValue;
  private Cart cart;

  @Before
  public void setup() {

    given(clock.getCurrentTime()).willReturn(SOME_TIME);
    cart = new Cart();
    cart.add(SOME_TITLE, SOME_QUANTITY);

    given(stockRepository.getByTitle(SOME_TITLE)).willReturn(stock);
    given(stockRepository.getByTitle(SOME_TITLE).getValue()).willReturn(stockValue);
    given(stockRepository.getByTitle(SOME_TITLE).getValue().getCurrentValue()).willReturn(DEFAULT_AMOUNT);

    factory = new TransactionFactory(clock, stockRepository);
  }

  @Test
  public void whenCreate_thenTypeIsSetToTransaction() {
    Transaction transaction = factory.create(cart);
    Transaction expected = new TransactionBuilder().withTime(clock.getCurrentTime());

    assertThat(transaction).isEqualToComparingOnlyGivenFields(expected, "type");
  }

  @Test
  public void whenCreate_thenLocalTimeSetToTransaction() {
    Transaction transaction = factory.create(cart);
    Transaction expected = new TransactionBuilder().withTime(clock.getCurrentTime());

    assertThat(transaction.getTime()).isEqualTo(expected.getTime());
  }

  @Test
  public void whenCreate_thenTransactionItemsIsSetToTrsanction() {
    Transaction transaction = factory.create(cart);
    Transaction expected = new TransactionBuilder().withTime(clock.getCurrentTime());

    assertThat(transaction.getListItems().get(0)).isEqualToComparingOnlyGivenFields(expected.getListItems().get(0), "stockId", "quantity");
    assertThat(transaction.getListItems().get(0).amount.getAmount()).isEqualToComparingFieldByField(expected.getListItems().get(0).amount.getAmount());
  }
}