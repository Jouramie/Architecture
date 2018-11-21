package ca.ulaval.glo4003.ws.api.cart.mappers;

import ca.ulaval.glo4003.service.cart.exceptions.EmptyCartOnCheckoutException;
import ca.ulaval.glo4003.ws.api.ErrorMapper;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class EmptyCartOnCheckoutExceptionMapper
    implements ExceptionMapper<EmptyCartOnCheckoutException> {

  @Override
  public Response toResponse(EmptyCartOnCheckoutException e) {
    return Response.status(Response.Status.BAD_REQUEST).build();
  }
}
