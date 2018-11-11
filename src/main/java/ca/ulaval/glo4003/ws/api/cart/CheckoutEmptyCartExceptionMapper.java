package ca.ulaval.glo4003.ws.api.cart;

import ca.ulaval.glo4003.domain.user.exceptions.EmptyCartException;
import ca.ulaval.glo4003.ws.api.ErrorMapper;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class CheckoutEmptyCartExceptionMapper
    implements ExceptionMapper<EmptyCartException> {

  @Override
  public Response toResponse(EmptyCartException e) {
    return Response.status(Response.Status.BAD_REQUEST).build();
  }
}
