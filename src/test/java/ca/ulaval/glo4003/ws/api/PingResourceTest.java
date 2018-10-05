package ca.ulaval.glo4003.ws.api;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.ws.api.ping.PingDto;
import ca.ulaval.glo4003.ws.api.ping.PingResource;
import ca.ulaval.glo4003.ws.api.ping.PingResourceImpl;
import org.junit.Test;

public class PingResourceTest {

  private final PingResource pingResource = new PingResourceImpl();

  @Test
  public void whenPinging_thenReturnPingDto() {
    PingDto ping = pingResource.ping("echo");

    assertThat(ping).isNotNull();
    assertThat(ping.echo).isEqualTo("echo");
  }
}
