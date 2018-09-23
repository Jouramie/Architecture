package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.MoneyAmount;

public class StockValue {

    private MoneyAmount open;
    private MoneyAmount close;
    private MoneyAmount current;

    public StockValue() {

    }

    public StockValue(MoneyAmount open, MoneyAmount close, MoneyAmount current) {
        this.open = open;
        this.close = close;
        this.current = current;

    }

    public MoneyAmount getOpenValue() {
        return open;
    }

    public MoneyAmount getCloseValue() {
        return close;
    }

    public MoneyAmount getCurrentValue() {
        return current;
    }
}

