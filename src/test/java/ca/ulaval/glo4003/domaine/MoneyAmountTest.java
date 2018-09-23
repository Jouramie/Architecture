package ca.ulaval.glo4003.domaine;

import ca.ulaval.glo4003.domain.MoneyAmount;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class MoneyAmountTest {
    private MoneyAmount money;
    private BigDecimal SOME_VALUE = new BigDecimal(225.8400);
    private BigDecimal SOME_USD_VALUE = new BigDecimal(225.8400);//usd

    @Before
    public void setupMoneyAmount() {
        money = new MoneyAmount(SOME_VALUE);
    }

    @Test
    public void whenGetValue_thenReturnCurrentValue() {
        // TODO: TDD TESTS
        // MoneyAmount result = money.getValue();
        //assertThat(result).isEqualTo(SOME_VALUE);
    }

    @Test
    public void whenConverterToUSD_thenReturnUSDMoney() {

    }
}