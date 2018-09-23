package ca.ulaval.glo4003.domaine;

import ca.ulaval.glo4003.domain.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockValue;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class StockValueTest {

    private MoneyAmount SOME_OPEN_VALUE = new MoneyAmount(new BigDecimal(225.8400));
    private MoneyAmount SOME_CLOSE_VALUE = new MoneyAmount(new BigDecimal(223.8400));
    private MoneyAmount SOME_CURRENT_VALUE = new MoneyAmount(new BigDecimal(0.000)); // TODO:
    private StockValue value;

    @Before
    public void setupStockValue() {
        value = new StockValue(SOME_OPEN_VALUE, SOME_CLOSE_VALUE, SOME_CURRENT_VALUE);
    }

    @Test
    public void whenGetOpenValue_thenReturnAMoneyAmountAtOpenning() {
        MoneyAmount result = value.getOpenValue();
        assertThat(result).isEqualTo(SOME_OPEN_VALUE);
    }

    @Test
    public void whenGetCloseValue_thenReturnAMoneyAmountAtClosed() {
        MoneyAmount result = value.getCloseValue();
        assertThat(result).isEqualTo(SOME_CLOSE_VALUE);
    }

    @Test
    public void whenGetCurrentStockValue_thenReturnCurrentMoneyAmount() {
        MoneyAmount result = value.getCurrentValue();
        assertThat(result).isEqualTo(SOME_CURRENT_VALUE);
    }
}