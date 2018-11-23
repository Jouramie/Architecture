package ca.ulaval.glo4003.ws.api.user.mappers;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import ca.ulaval.glo4003.service.user.UserDoesNotExistException;
import ca.ulaval.glo4003.ws.api.ErrorMapper;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class UserDoesNotExistExceptionMapper implements ExceptionMapper<UserDoesNotExistException> {
  @Override
  public Response toResponse(UserDoesNotExistException e) {
    return Response.status(NOT_FOUND).build();
  }
}
