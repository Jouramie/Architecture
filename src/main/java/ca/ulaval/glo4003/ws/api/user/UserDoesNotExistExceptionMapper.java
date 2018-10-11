package ca.ulaval.glo4003.ws.api.user;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import ca.ulaval.glo4003.infrastructure.injection.ErrorMapper;
import ca.ulaval.glo4003.service.user.UserDoesNotExistException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class UserDoesNotExistExceptionMapper implements ExceptionMapper<UserDoesNotExistException> {
  @Override
  public Response toResponse(UserDoesNotExistException e) {
    return Response.status(BAD_REQUEST).build();
  }
}
