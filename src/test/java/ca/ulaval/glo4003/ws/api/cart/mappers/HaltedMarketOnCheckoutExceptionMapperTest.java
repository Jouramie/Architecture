package ca.ulaval.glo4003.ws.api.cart.mappers;


import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.service.cart.HaltedMarketOnCheckoutException;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;

public class HaltedMarketOnCheckoutExceptionMapperTest {

  private HaltedMarketOnCheckoutExceptionMapper mapper;

  @Before
  public void setup() {
    mapper = new HaltedMarketOnCheckoutExceptionMapper();
  }

  @Test
  public void whenMappingException_thenResponseIsBadRequestWithHaltMessage() {
    String message = "market halted";

    Response response =mapper.toResponse(new HaltedMarketOnCheckoutException(message));

    assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
    assertThat(response.getEntity()).toString().contains(message);
  }
}
