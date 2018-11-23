package ca.ulaval.glo4003.domain.transaction;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.market.HaltedMarketException;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.states.Market;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@Component
public class TransactionFactory {
  private final Clock clock;
  private final StockRepository stockRepository;
  private final MarketRepository marketRepository;

  @Inject
  public TransactionFactory(Clock clock, StockRepository stockRepository, MarketRepository marketRepository) {
    this.clock = clock;
    this.stockRepository = stockRepository;
    this.marketRepository = marketRepository;
  }

  public Transaction createPurchase(StockCollection stocks) throws StockNotFoundException, HaltedMarketException {
    List<TransactionItem> transactionItems = buildTransactionItems(stocks);
    return new Transaction(clock.getCurrentTime(), transactionItems, TransactionType.PURCHASE);
  }

  private List<TransactionItem> buildTransactionItems(StockCollection items)
      throws StockNotFoundException, HaltedMarketException {
    List<TransactionItem> transactionItems = new ArrayList<>();
    for (String title : items.getTitles()) {
      validateMarketOfStockIsNotHalted(title);
      int quantity = items.getQuantity(title);
      transactionItems.add(buildTransactionItem(title, quantity));
    }
    return transactionItems;
  }

  private void validateMarketOfStockIsNotHalted(String title) throws HaltedMarketException, StockNotFoundException {
    try {
      Market market = marketRepository.findMarketForStock(title);
      if (market.isHalted()) {
        throw new HaltedMarketException(market.getHaltMessage());
      }
    } catch (MarketNotFoundException e) {
      throw new StockNotFoundException(title);
    }
  }

  private TransactionItem buildTransactionItem(String title, int quantity) throws StockNotFoundException {
    MoneyAmount amount = stockRepository.findByTitle(title).getValue().getLatestValue();
    return new TransactionItem(title, quantity, amount);
  }
}
