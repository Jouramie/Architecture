package ca.ulaval.glo4003.domain.money;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Currency {
    private final String name;
    private final BigDecimal rateToUsd;

    public Currency(String name, BigDecimal rateToUSD) {
        this.name = name;
        this.rateToUsd = rateToUSD;
    }

    public String getName() {
        return this.name;
    }

    public MoneyAmount convert(MoneyAmount amount) {
        BigDecimal factor = amount.getCurrency().rateToUsd.divide(this.rateToUsd, MathContext.DECIMAL64);
        return new MoneyAmount(amount.getAmount().multiply(factor).setScale(2, RoundingMode.HALF_EVEN), this);
    }

    public BigDecimal toUsd(BigDecimal amount) {
        return amount.multiply(this.rateToUsd).setScale(2, RoundingMode.HALF_EVEN);
    }
}
