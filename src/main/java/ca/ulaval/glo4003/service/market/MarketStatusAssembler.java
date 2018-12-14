package ca.ulaval.glo4003.service.market;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.market.state.Market;

@Component
public class MarketStatusAssembler {

  public MarketStatusDto toDto(Market market) {
    return new MarketStatusDto(
        market.getId(),
        market.isHalted(),
        market.getHaltMessage());
  }
}
