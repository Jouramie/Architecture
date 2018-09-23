package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.market.MarketId;

import java.util.List;

public interface StockRepository {
    List<Stock> getAll();
    Stock getByTitle(String title);
    Stock getByName(String name);
    List<Stock> getStocksOfMarket(MarketId marketId);

    void add(Stock stock);
}
