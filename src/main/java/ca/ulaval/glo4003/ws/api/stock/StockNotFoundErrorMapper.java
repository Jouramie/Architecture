package ca.ulaval.glo4003.ws.api.stock;

import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.ws.infrastructure.injection.ErrorMapper;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class StockNotFoundErrorMapper implements ExceptionMapper<StockNotFoundException> {

  @Override
  public Response toResponse(StockNotFoundException e) {
    return Response.status(Response.Status.NOT_FOUND).build();
  }
}
