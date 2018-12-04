package ca.ulaval.glo4003.ws.api.market;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.service.market.MarketDoesNotExistException;
import ca.ulaval.glo4003.service.market.MarketService;
import ca.ulaval.glo4003.service.market.MarketStatusDto;
import ca.ulaval.glo4003.ws.api.market.assemblers.ApiMarketStatusAssembler;
import ca.ulaval.glo4003.ws.api.market.dto.MarketStatusResponseDto;
import ca.ulaval.glo4003.ws.http.authentication.AuthenticationRequiredBinding;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/markets/{market}")
@Produces(MediaType.APPLICATION_JSON)
@Resource
public class MarketHaltResourceImpl implements DocumentedMarketHaltResource {

  private final MarketService marketService;
  private final ApiMarketStatusAssembler apiMarketStatusAssembler;

  @Inject
  public MarketHaltResourceImpl(MarketService marketService, ApiMarketStatusAssembler apiMarketStatusAssembler) {
    this.marketService = marketService;
    this.apiMarketStatusAssembler = apiMarketStatusAssembler;
  }

  @POST
  @Path("/halt")
  @AuthenticationRequiredBinding(acceptedRoles = UserRole.ADMINISTRATOR)
  @Override
  public MarketStatusResponseDto haltMarket(@PathParam("market") String market, @QueryParam("message") String message)
      throws MarketDoesNotExistException {
    MarketId marketId = new MarketId(market);
    marketService.haltMarket(marketId, message);
    MarketStatusDto marketStatus = marketService.getMarketStatus(marketId);
    return apiMarketStatusAssembler.toDto(marketStatus);
  }

  @POST
  @Path("/resume")
  @AuthenticationRequiredBinding(acceptedRoles = UserRole.ADMINISTRATOR)
  @Override
  public MarketStatusResponseDto resumeMarket(@PathParam("market") String market) throws MarketDoesNotExistException {
    MarketId marketId = new MarketId(market);
    marketService.resumeMarket(marketId);
    MarketStatusDto marketStatus = marketService.getMarketStatus(marketId);
    return apiMarketStatusAssembler.toDto(marketStatus);
  }
}
