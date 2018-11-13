package ca.ulaval.glo4003.ws.api.authentication;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static junit.framework.TestCase.assertEquals;

import ca.ulaval.glo4003.service.authentication.AuthenticationErrorException;
import ca.ulaval.glo4003.ws.api.authentication.mappers.AuthenticationExceptionMapper;
import javax.ws.rs.core.Response;
import org.junit.Test;

public class AuthenticationExceptionMapperTest {

  private final AuthenticationExceptionMapper errorMapper = new AuthenticationExceptionMapper();

  @Test
  public void whenMappingError_thenReturn400BadRequest() {
    Response response = errorMapper.toResponse(new AuthenticationErrorException());

    assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
  }
}
