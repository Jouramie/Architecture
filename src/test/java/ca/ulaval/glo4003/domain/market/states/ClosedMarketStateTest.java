package ca.ulaval.glo4003.domain.market.states;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;

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
public class ClosedMarketStateTest {
  private final Market market = new TestingMarketBuilder().build();
  @Mock
  private StockValueRetriever stockValueRetriever;
  @Mock
  private Stock stockMock;
  private ClosedMarketState state;

  @Before
  public void setupClosedMarketState() {
    state = new ClosedMarketState();
  }

  @Test
  public void whenTimeDoesNotOpenTheMarket_thenStayCloseAndValuesAreNotUpdated() {
    LocalDateTime someCloseTime = LocalDateTime.of(LocalDate.now(), market.closingTime.plusMinutes(1));

    MarketState newState = state.update(market, someCloseTime, stockValueRetriever);

    verify(stockValueRetriever, never()).updateStockValue(any(Stock.class));
    assertThat(newState).isInstanceOf(ClosedMarketState.class);
  }

  @Test
  public void whenTimeClosesTheMarket_thenMarketOpensAndOpeningPriceIsSaved() {
    LocalDateTime someOpenedTime = LocalDateTime.of(LocalDate.now(), market.openingTime.plusMinutes(1));
    Market market = new TestingMarketBuilder().withStocks(Collections.singletonList(stockMock)).build();

    MarketState newState = state.update(market, someOpenedTime, stockValueRetriever);

    verify(stockMock).open();

    assertThat(newState).isInstanceOf(OpenMarketState.class);
  }
}
