package ca.ulaval.glo4003.ws.api.stock.mapper;

import ca.ulaval.glo4003.service.stock.StockDoesNotExistException;
import ca.ulaval.glo4003.ws.api.ErrorMapper;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class StockDoesNotExistExceptionMapper implements ExceptionMapper<StockDoesNotExistException> {

  @Override
  public Response toResponse(StockDoesNotExistException e) {
    return Response.status(Response.Status.NOT_FOUND).build();
  }
}
