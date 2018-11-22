package ca.ulaval.glo4003.domain.market.states;

public class TradingStatus {

  public final boolean isHalted;
  public final String haltMessage;

  public TradingStatus(boolean isHalted, String haltMessage) {
    this.isHalted = isHalted;
    this.haltMessage = haltMessage;
  }
}
