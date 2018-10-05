package ca.ulaval.glo4003.ws.api.authentication;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import ca.ulaval.glo4003.domain.user.authentication.AuthenticationErrorException;
import ca.ulaval.glo4003.infrastructure.injection.ErrorMapper;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class AuthenticationErrorMapper implements ExceptionMapper<AuthenticationErrorException> {

  @Override
  public Response toResponse(AuthenticationErrorException e) {
    return Response.status(BAD_REQUEST).build();
  }
}
