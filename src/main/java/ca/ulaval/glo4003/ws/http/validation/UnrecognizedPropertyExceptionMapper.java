package ca.ulaval.glo4003.ws.http.validation;

import ca.ulaval.glo4003.ws.api.ErrorMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class UnrecognizedPropertyExceptionMapper implements ExceptionMapper<UnrecognizedPropertyException> {

  @Override
  public Response toResponse(UnrecognizedPropertyException exception) {
    return Response.status(Response.Status.BAD_REQUEST).build();
  }
}

