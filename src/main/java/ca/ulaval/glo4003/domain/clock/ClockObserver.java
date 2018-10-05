package ca.ulaval.glo4003.domain.clock;

import java.time.LocalDateTime;

public interface ClockObserver {
  void onTick(LocalDateTime currentTime);
}
