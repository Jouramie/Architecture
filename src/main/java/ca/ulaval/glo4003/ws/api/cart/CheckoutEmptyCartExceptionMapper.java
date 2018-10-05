package ca.ulaval.glo4003.ws.api.cart;

import ca.ulaval.glo4003.service.cart.CheckoutEmptyCartException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class CheckoutEmptyCartExceptionMapper implements ExceptionMapper<CheckoutEmptyCartException> {

  @Override
  public Response toResponse(CheckoutEmptyCartException e) {
    return Response.status(Response.Status.BAD_REQUEST).build();
  }
}
