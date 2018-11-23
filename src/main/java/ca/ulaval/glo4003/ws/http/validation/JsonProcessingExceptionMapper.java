package ca.ulaval.glo4003.ws.http.validation;

import ca.ulaval.glo4003.ws.api.ErrorMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@ErrorMapper
public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {

  @Override
  public Response toResponse(JsonProcessingException eexception) {
    return Response.status(Response.Status.BAD_REQUEST).build();
  }
}

