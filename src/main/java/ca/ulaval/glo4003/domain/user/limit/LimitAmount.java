package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import org.joda.time.DateTime;

public class LimitAmount extends Limit {
    private final MoneyAmount amount;

    LimitAmount(ApplicationPeriod period, DateTime start, MoneyAmount amount) {
        super(period, start);
        this.amount = amount;
    }
}
