package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.money.MoneyAmount;

public class Stock {
    private final String title;
    private final String name;
    private final MarketId marketId;
    private StockValue value;

    public Stock(String title, String name, MarketId marketId, MoneyAmount startValue) {
        this.title = title;
        this.name = name;
        this.marketId = marketId;
        this.value = new StockValue(startValue);
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
        MoneyAmount moneyVariation = new MoneyAmount(variation, this.value.getCurrentValue().getCurrency());
        MoneyAmount newMoneyAmount = this.value.getCurrentValue().add(moneyVariation);
        this.value.setValue(newMoneyAmount);
    }

    public synchronized StockValue getValue() {
        return this.value;
    }

    public synchronized void close() {
        this.value.close();
    }
}
