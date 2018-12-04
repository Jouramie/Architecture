package ca.ulaval.glo4003.domain.market.states;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.verify;

import ca.ulaval.glo4003.domain.market.MarketState;
import ca.ulaval.glo4003.domain.market.TestingMarketBuilder;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OpenMarketStateTest {
  private final Market market = new TestingMarketBuilder().build();
  @Mock
  private StockValueRetriever stockValueRetriever;
  @Mock
  private Stock stockMock;
  private OpenMarketState state;

  @Before
  public void setupOpenMarketState() {
    state = new OpenMarketState();
  }

  @Test
  public void whenTimeDoesNotCloseTheMarket_thenUpdateAllStockValuesAndStayOpen() {
    LocalDateTime someOpenTime = LocalDateTime.of(LocalDate.now(), market.openingTime.plusMinutes(1));

    MarketState newState = state.update(market, someOpenTime, stockValueRetriever);

    verify(stockValueRetriever).updateStockValue(market.stocks.get(0));
    assertThat(newState).isInstanceOf(OpenMarketState.class);
  }

  @Test
  public void whenTimeClosesTheMarket_thenUpdateAllStockAndChangeStateToClose() {
    LocalDateTime someClosedTime = LocalDateTime.of(LocalDate.now(), market.closingTime.plusMinutes(1));
    Market market = new TestingMarketBuilder().withStocks(Collections.singletonList(stockMock)).build();

    MarketState newState = state.update(market, someClosedTime, stockValueRetriever);

    verify(stockValueRetriever).updateStockValue(market.stocks.get(0));
    assertThat(newState).isInstanceOf(ClosedMarketState.class);
  }

  @Test
  public void whenTimeClosesTheMarket_thenClosingPricesAreSaved() {
    LocalDateTime someClosedTime = LocalDateTime.of(LocalDate.now(), market.closingTime.plusMinutes(1));
    Market market = new TestingMarketBuilder().withStocks(Collections.singletonList(stockMock)).build();

    state.update(market, someClosedTime, stockValueRetriever);

    verify(stockMock).close();
  }
}
