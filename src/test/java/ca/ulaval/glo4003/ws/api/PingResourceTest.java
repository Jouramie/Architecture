package ca.ulaval.glo4003.ws.api;

import ca.ulaval.glo4003.ws.api.dto.PingDto;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PingResourceTest {

    private PingResource pingResource = new PingResource();

    @Test
    public void whenPinging_thenReturnPingDto() {
        PingDto ping = pingResource.ping();

        assertThat(ping).isNotNull();
    }
}
