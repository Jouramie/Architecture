package ca.ulaval.glo4003.service.market;

import ca.ulaval.glo4003.domain.market.MarketId;

public class MarketStatusDto {
  public final MarketId marketId;
  public final boolean isHalted;
  public final String haltMessage;

  public MarketStatusDto(MarketId marketId, boolean isHalted, String haltMessage) {
    this.marketId = marketId;
    this.isHalted = isHalted;
    this.haltMessage = haltMessage;
  }
}
