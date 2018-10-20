package ca.ulaval.glo4003.ws.api.validation;

import ca.ulaval.glo4003.ws.api.ErrorMapper;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class InvalidInputExceptionMapper implements ExceptionMapper<InvalidInputException> {

  @Override
  public Response toResponse(InvalidInputException e) {
    return Response.status(Response.Status.BAD_REQUEST).entity(e.getInputErrors()).build();
  }
}
