package ca.ulaval.glo4003.ws.api.market.halt;

import ca.ulaval.glo4003.service.market.MarketDoesNotExistException;
import ca.ulaval.glo4003.ws.api.ErrorMapper;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class MarketDoesNotExistExceptionMapper implements ExceptionMapper<MarketDoesNotExistException> {
  @Override
  public Response toResponse(MarketDoesNotExistException e) {
    return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
  }
}
