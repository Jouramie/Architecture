package ca.ulaval.glo4003.ws.api.cart;

import ca.ulaval.glo4003.service.cart.exceptions.InvalidStockTitleException;
import ca.ulaval.glo4003.ws.api.ErrorMapper;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class InvalidStockTitleExceptionMapper
    implements ExceptionMapper<InvalidStockTitleException> {

  @Override
  public Response toResponse(InvalidStockTitleException e) {
    return Response.status(Response.Status.BAD_REQUEST).build();
  }
}