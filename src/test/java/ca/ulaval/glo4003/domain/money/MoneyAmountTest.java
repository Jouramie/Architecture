package ca.ulaval.glo4003.domain.money;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class MoneyAmountTest {
    private final double SOME_AMOUNT = 12.34;
    private final double SOME_OTHER_AMOUNT = 46.78;
    private final double SOME_CONVERTED_AMOUNT = 52.25;

    @Mock
    Currency someCurrency;

    private MoneyAmount amount;

    @Before
    public void setupMoneyAmount() {
        amount = new MoneyAmount(SOME_AMOUNT, someCurrency);
    }

    @Test
    public void whenGetAmount_thenReturnAmountWithScaleOf2() {
        BigDecimal result = amount.getAmount();

        assertThat(result.doubleValue()).isEqualTo(SOME_AMOUNT);
        assertThat(result.scale()).isEqualTo(2);
    }

    @Test
    public void whenGetCurrency_thenReturnConfiguredCurrency() {
        Currency result = amount.getCurrency();

        assertThat(result).isEqualTo(someCurrency);
    }

    @Test
    public void whenToUsd_thenReturnValueCalculatedByCurrency() {
        given(someCurrency.toUsd(any())).willReturn(new BigDecimal(SOME_OTHER_AMOUNT));

        BigDecimal amountInUsd = amount.toUsd();

        assertThat(amountInUsd.doubleValue()).isEqualTo(SOME_OTHER_AMOUNT);
    }

    @Test
    public void givenOtherAmountInSameCurrency_whenAdd_thenAddTheAmountAndReturnInstanceWithSameCurrency() {
        MoneyAmount otherAmount = new MoneyAmount(SOME_OTHER_AMOUNT, someCurrency);
        given(someCurrency.convert(otherAmount)).willReturn(otherAmount);

        MoneyAmount result = amount.add(otherAmount);

        assertThat(result.getAmount().doubleValue()).isEqualTo(59.12);
        assertThat(result.getCurrency()).isEqualTo(someCurrency);
    }

    @Test
    public void givenOtherAmountInDifferentCurrency_whenAdd_thenConvertTheAmountAndAddItToTheReturnedInstance() {
        Currency otherCurrency = new Currency("EURO", new BigDecimal(0.86));
        MoneyAmount otherAmount = new MoneyAmount(SOME_OTHER_AMOUNT, otherCurrency);
        MoneyAmount resultAmount = new MoneyAmount(SOME_CONVERTED_AMOUNT, someCurrency);
        given(someCurrency.convert(otherAmount)).willReturn(resultAmount);

        MoneyAmount result = amount.add(otherAmount);

        assertThat(result.getAmount().doubleValue()).isEqualTo(64.59);
        assertThat(result.getCurrency()).isEqualTo(someCurrency);
    }

    @Test
    public void givenOtherAmountInSameCurrency_whenSubtract_thenAddTheAmountAndReturnInstanceWithSameCurrency() {
        MoneyAmount otherAmount = new MoneyAmount(SOME_OTHER_AMOUNT, someCurrency);
        given(someCurrency.convert(otherAmount)).willReturn(otherAmount);

        MoneyAmount result = amount.subtract(otherAmount);

        assertThat(result.getAmount().doubleValue()).isEqualTo(-34.44);
        assertThat(result.getCurrency()).isEqualTo(someCurrency);
    }

    @Test
    public void givenOtherAmountInDifferentCurrency_whenSubtract_thenConvertTheAmountAndAddItToTheReturnedInstance() {
        Currency otherCurrency = new Currency("EURO", new BigDecimal(0.86));
        MoneyAmount otherAmount = new MoneyAmount(SOME_OTHER_AMOUNT, otherCurrency);
        MoneyAmount resultAmount = new MoneyAmount(SOME_CONVERTED_AMOUNT, someCurrency);
        given(someCurrency.convert(otherAmount)).willReturn(resultAmount);

        MoneyAmount result = amount.subtract(otherAmount);

        assertThat(result.getAmount().doubleValue()).isEqualTo(-39.91);
        assertThat(result.getCurrency()).isEqualTo(someCurrency);
    }
}
