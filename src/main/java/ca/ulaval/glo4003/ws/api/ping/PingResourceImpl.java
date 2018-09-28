package ca.ulaval.glo4003.ws.api.ping;

import javax.annotation.Resource;
import javax.ws.rs.BadRequestException;

@Resource
public class PingResourceImpl implements PingResource {
  @Override
  public PingDto ping(String echo) {
    if (echo == null || echo.isEmpty()) {
      throw new BadRequestException("Missing echo query parameter");
    }
    return new PingDto(echo);
  }
}
