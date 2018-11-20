package ca.ulaval.glo4003.ws.api.cart.mappers;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import ca.ulaval.glo4003.service.cart.HaltedMarketOnCheckoutException;
import ca.ulaval.glo4003.ws.api.ErrorMapper;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class HaltedMarketOnCheckoutExceptionMapper implements ExceptionMapper<HaltedMarketOnCheckoutException> {
  @Override
  public Response toResponse(HaltedMarketOnCheckoutException e) {
    return Response.status(BAD_REQUEST).entity(e.message).build();
  }
}
