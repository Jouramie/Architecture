package ca.ulaval.glo4003.infrastructure.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValue;
import ca.ulaval.glo4003.util.TransactionItemBuilder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
  private static final List<TransactionItem> items = Arrays.asList(new TransactionItemBuilder().buildDefault());
  private static final TransactionType SOME_TYPE = TransactionType.PURCHASE;
  private static final LocalDateTime SOME_TIME = LocalDateTime.now();
  private static final int SOME_QUANTITY = 1;
  private static final String SOME_TITLE = "title";
  private static Cart cart;
  private TransactionFactory factory;
  @Mock
  private Clock clock;
  @Mock
  private StockRepository stockRepository;
  @Mock
  private Stock stock;
  @Mock
  private StockValue stockValue;

  @Before
  public void setup() {

    given(clock.getCurrentTime()).willReturn(SOME_TIME);
    given(stockRepository.getByTitle(SOME_TITLE)).willReturn(stock);
    given(stockRepository.getByTitle(SOME_TITLE).getValue()).willReturn(stockValue);
    given(stockRepository.getByTitle(SOME_TITLE).getValue().getCurrentValue()).willReturn(DEFAULT_AMOUNT);
    cartBuild();
    factory = new TransactionFactory(clock, stockRepository);
  }

  @Test
  public void whenCreateTransaction_thenReturnCreatedTransaction() {
    Transaction transaction = factory.create(cart);//TODO: finish this tests
    Transaction expectedTransaction = new Transaction(clock, items, SOME_TYPE);
    assertThat(transaction).extracting("type").containsExactly(
        SOME_TYPE);
  }

  public void cartBuild() {
    cart = new Cart();
    cart.add(SOME_TITLE, SOME_QUANTITY);
  }
}