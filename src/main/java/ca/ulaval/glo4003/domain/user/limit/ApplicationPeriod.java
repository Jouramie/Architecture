package ca.ulaval.glo4003.domain.user.limit;

import java.time.Duration;

public enum ApplicationPeriod {
    DAILY(Duration.ofHours(24)),
    WEEKLY(Duration.ofDays(7)),
    MONTHLY(Duration.ofDays(30));

    private Duration duration;

    ApplicationPeriod(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }
}
