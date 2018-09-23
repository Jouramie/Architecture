package ca.ulaval.glo4003.domain;

import java.math.BigDecimal;

public class MoneyAmount {
    private BigDecimal amount;

    public MoneyAmount(BigDecimal amountValue) {
        this.amount = amountValue;
    }

    public BigDecimal getValue() {
        return amount;
    }
}

