package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.market.MarketId;

public class Stock {
    private final String title;
    private final String name;
    private final MarketId marketId;
    private double value;

    public Stock(String title, String name, MarketId marketId) {
        this.title = title;
        this.name = name;
        this.marketId = marketId;
        this.value = 100.0;
    }

    public String getTitle() {
        return this.title;
    }

    public String getName() {
        return this.name;
    }

    public MarketId getMarketId() {
        return this.marketId;
    }

    public synchronized void updateValue(double variation) {
        this.value += variation;
    }

    // TODO: use MoneyAmount instead of double.
    public synchronized double getValue() {
        return this.value;
    }
}
