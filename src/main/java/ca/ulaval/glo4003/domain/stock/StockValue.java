package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.money.MoneyAmount;

public class StockValue {
    private MoneyAmount currentValue;
    private MoneyAmount openValue;
    private MoneyAmount closeValue;

    public StockValue(MoneyAmount startValue) {
        this.currentValue = startValue;
        this.openValue = startValue;
        this.closeValue = startValue;
    }

    public MoneyAmount getCurrentValue() {
        return this.currentValue;
    }

    public MoneyAmount getOpenValue() {
        return this.openValue;
    }

    public MoneyAmount getCloseValue() {
        return this.closeValue;
    }

    public boolean isClosed() {
        return this.closeValue != null;
    }

    public void setValue(MoneyAmount currentValue) {
        if(this.closeValue != null) {
            this.openValue = currentValue;
            this.closeValue = null;
        }

        this.currentValue = currentValue;
    }

    public void close() {
        this.closeValue = this.currentValue;
    }
}
