package ca.ulaval.glo4003.ws.api.util;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.service.date.SinceParameter;
import javax.ws.rs.BadRequestException;

@Component
public class SinceParameterConverter {
  public SinceParameter convertSinceParameter(String since) throws BadRequestException {
    if (since == null) {
      throw new BadRequestException("Missing 'since' parameter");
    }

    try {
      return SinceParameter.valueOf(since);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid 'since' parameter");
    }
  }
}
