package ca.ulaval.glo4003.ws.api.ping;

import ca.ulaval.glo4003.domain.clock.Clock;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

@Resource
public class PingResourceImpl implements PingResource {
  private final Clock clock;

  @Inject
  public PingResourceImpl(Clock clock) {
    this.clock = clock;
  }

  @Override
  public PingDto ping(String echo) {
    if (echo == null || echo.isEmpty()) {
      throw new BadRequestException("Missing echo query parameter");
    }
    return new PingDto(clock.getCurrentTime(), echo);
  }
}
