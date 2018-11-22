package ca.ulaval.glo4003.ws.api.market.halt;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/markets/{market}/resume")
@Produces(MediaType.APPLICATION_JSON)
public interface MarketResumeResource {


}
