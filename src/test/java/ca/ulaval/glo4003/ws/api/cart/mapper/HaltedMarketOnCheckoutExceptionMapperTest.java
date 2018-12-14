package ca.ulaval.glo4003.ws.api.cart.mapper;


import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.service.cart.exception.HaltedMarketOnCheckoutException;
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

    Response response = mapper.toResponse(new HaltedMarketOnCheckoutException(message));

    assertThat(response.getStatus()).isEqualTo(FORBIDDEN.getStatusCode());
    assertThat(response.getEntity()).toString().contains(message);
  }
}
