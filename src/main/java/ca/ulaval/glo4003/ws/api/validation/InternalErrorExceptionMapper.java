package ca.ulaval.glo4003.ws.api.validation;

import ca.ulaval.glo4003.ws.api.ErrorMapper;
import ca.ulaval.glo4003.service.InternalErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class InternalErrorExceptionMapper implements ExceptionMapper<InternalErrorException> {
  @Override
  public Response toResponse(InternalErrorException e) {
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
  }
}
