package ca.ulaval.glo4003.ws.api;

import static junit.framework.TestCase.assertTrue;

import ca.ulaval.glo4003.ws.api.dto.PingDto;

import org.junit.Test;

public class PingResourceTest {

    private PingResource pingResource = new PingResource();

    @Test
    public void whenPinging_thenReturnPingDto() {
        PingDto ping = pingResource.ping();

        assertTrue(ping != null);
    }
}
