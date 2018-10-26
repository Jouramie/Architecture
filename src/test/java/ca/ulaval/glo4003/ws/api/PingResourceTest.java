package ca.ulaval.glo4003.ws.api;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.ws.api.ping.PingDto;
import ca.ulaval.glo4003.ws.api.ping.PingResource;
import ca.ulaval.glo4003.ws.api.ping.PingResourceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PingResourceTest {
  @Mock
  private Clock clock;

  private PingResource pingResource;

  @Before
  public void setupPingResource() {
    pingResource = new PingResourceImpl(clock);
  }

  @Test
  public void whenPinging_thenReturnPingDto() {
    PingDto ping = pingResource.ping("echo");

    assertThat(ping).isNotNull();
    assertThat(ping.echo).isEqualTo("echo");
  }
}
