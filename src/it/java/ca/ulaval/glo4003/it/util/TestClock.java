package ca.ulaval.glo4003.it.util;

import ca.ulaval.glo4003.domain.clock.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestClock extends Clock {
  private LocalDateTime currentTime;

  public TestClock(LocalDateTime startTime) {
    super(startTime, Duration.ZERO);
    currentTime = startTime;
  }

  @Override
  public LocalDateTime getCurrentTime() {
    return currentTime;
  }

  public void setCurrentTime(LocalDateTime currentTime) {
    this.currentTime = currentTime;
  }

  @Override
  public LocalDate getCurrentDate() {
    return currentTime.toLocalDate();
  }
}
