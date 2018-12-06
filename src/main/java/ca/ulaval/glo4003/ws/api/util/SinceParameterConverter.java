package ca.ulaval.glo4003.ws.api.util;

import ca.ulaval.glo4003.service.date.Since;
import javax.ws.rs.BadRequestException;

public class SinceParameterConverter {
  public static Since convertSinceParameter(String since) throws BadRequestException {
    if (since == null) {
      throw new BadRequestException("Missing 'since' parameter");
    }

    try {
      return Since.valueOf(since);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid 'since' parameter");
    }
  }
}
