package ca.ulaval.glo4003.ws.api.cart;

import ca.ulaval.glo4003.domain.cart.StockNotInCartException;
import ca.ulaval.glo4003.infrastructure.injection.ErrorMapper;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class StockNotInCartExceptionMapper implements ExceptionMapper<StockNotInCartException> {

  @Override
  public Response toResponse(StockNotInCartException e) {
    return Response.status(Response.Status.BAD_REQUEST).build();
  }
}