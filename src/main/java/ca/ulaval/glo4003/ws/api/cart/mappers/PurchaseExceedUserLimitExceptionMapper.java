package ca.ulaval.glo4003.ws.api.cart.mappers;

import ca.ulaval.glo4003.service.cart.exceptions.PurchaseExceedUserLimitException;
import ca.ulaval.glo4003.ws.api.ErrorMapper;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class PurchaseExceedUserLimitExceptionMapper implements ExceptionMapper<PurchaseExceedUserLimitException> {

  @Override
  public Response toResponse(PurchaseExceedUserLimitException e) {
    return Response.status(Response.Status.FORBIDDEN).build();
  }
}
