package ca.ulaval.glo4003.ws.api.authentication;

import ca.ulaval.glo4003.domain.user.UserAlreadyExistsException;
import ca.ulaval.glo4003.infrastructure.injection.ErrorMapper;
import ca.ulaval.glo4003.service.authentication.InvalidUserEmailException;
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
