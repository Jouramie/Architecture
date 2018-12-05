package ca.ulaval.glo4003.domain.clock;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ReadableClock {
  LocalDateTime getCurrentTime();

  LocalDate getCurrentDate();
}
