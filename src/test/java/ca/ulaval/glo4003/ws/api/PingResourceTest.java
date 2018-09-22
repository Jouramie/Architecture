package ca.ulaval.glo4003.ws.api;

import ca.ulaval.glo4003.ws.api.dto.PingDto;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PingResourceTest {

  private final PingResource pingResource = new PingResourceImpl();

  @Test
  public void whenPinging_thenReturnPingDto() {
    PingDto ping = pingResource.ping("echo");

    assertThat(ping).isNotNull();
    assertThat(ping.echo).isEqualTo("echo");
  }
}
