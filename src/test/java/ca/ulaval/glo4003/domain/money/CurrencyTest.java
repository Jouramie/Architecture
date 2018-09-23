package ca.ulaval.glo4003.domain.money;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyTest {
    private final String SOME_NAME = "CAD";
    private final BigDecimal SOME_RATE_TO_USD = new BigDecimal(0.77);

    private Currency currency;

    @Before
    public void setupCurrency() {
        currency = new Currency(SOME_NAME, SOME_RATE_TO_USD);
    }

    @Test
    public void whenGetName_thenReturnName() {
        String name = currency.getName();

        assertThat(name).isEqualTo(SOME_NAME);
    }

    @Test
    public void whenConvert_thenApplyProperConversionFactor() {
        MoneyAmount amount = new MoneyAmount(57.83, new Currency("Euronext", new BigDecimal(0.86)));

        MoneyAmount result = currency.convert(amount);

        assertThat(result.getAmount().doubleValue()).isEqualTo(64.59);
        assertThat(result.getCurrency()).isEqualTo(currency);
    }

    @Test
    public void whenToUsd_thenApplyRateToAmountWithAppropriateScale() {
        BigDecimal result = currency.toUsd(new BigDecimal(12.34));

        assertThat(result).isEqualTo(new BigDecimal(9.50).setScale(2));
        assertThat(result.scale()).isEqualTo(2);
    }
}
