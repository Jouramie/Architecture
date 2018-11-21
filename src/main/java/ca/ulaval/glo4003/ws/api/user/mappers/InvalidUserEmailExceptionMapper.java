package ca.ulaval.glo4003.ws.api.user.mappers;

import ca.ulaval.glo4003.service.user.InvalidUserEmailException;
import ca.ulaval.glo4003.ws.api.ErrorMapper;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class InvalidUserEmailExceptionMapper
    implements ExceptionMapper<InvalidUserEmailException> {

  @Override
  public Response toResponse(InvalidUserEmailException e) {
    return Response.status(Response.Status.BAD_REQUEST).build();
  }
}
