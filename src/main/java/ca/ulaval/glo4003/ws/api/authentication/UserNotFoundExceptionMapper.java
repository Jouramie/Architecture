package ca.ulaval.glo4003.ws.api.authentication;

import ca.ulaval.glo4003.ws.domain.user.UserNotFoundException;
import ca.ulaval.glo4003.ws.infrastructure.injection.ErrorMapper;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class UserNotFoundExceptionMapper implements ExceptionMapper<UserNotFoundException> {

  @Override
  public Response toResponse(UserNotFoundException e) {
    return Response.status(Response.Status.BAD_REQUEST).build();
  }
}
