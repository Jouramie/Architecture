package ca.ulaval.glo4003.domain.money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyAmount {
    private final BigDecimal amount;
    private final Currency currency;

    public MoneyAmount(double amount, Currency currency) {
        this.amount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_EVEN);
        this.currency = currency;
    }

    public MoneyAmount(BigDecimal amount, Currency currency) {
        this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public MoneyAmount add(MoneyAmount otherAmount) {
        MoneyAmount convertedAmount = this.currency.convert(otherAmount);
        return new MoneyAmount(this.amount.add(convertedAmount.getAmount()), this.getCurrency());
    }

    public MoneyAmount subtract(MoneyAmount otherAmount) {
        MoneyAmount convertedAmount = this.currency.convert(otherAmount);
        return new MoneyAmount(this.amount.subtract(convertedAmount.getAmount()), this.getCurrency());
    }

    public BigDecimal toUsd() {
        return this.currency.toUsd(this.amount);
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) return true;
        if(!(other instanceof MoneyAmount)) return false;

        final MoneyAmount otherAmount = (MoneyAmount)other;
        return this.getAmount().equals(otherAmount.getAmount()) &&
               this.getCurrency().equals(otherAmount.getCurrency());
    }

    @Override
    public int hashCode() {
        return this.getAmount().hashCode() ^ this.getCurrency().hashCode();
    }
}
