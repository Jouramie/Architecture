package ca.ulaval.glo4003.domain.market.states;

public class TradingStatus {

  public final boolean isHalted;
  public final String haltMessage;

  private TradingStatus(boolean isHalted, String haltMessage) {
    this.isHalted = isHalted;
    this.haltMessage = haltMessage;
  }

  public static TradingStatus halted(String message) {
    return new TradingStatus(true, message);
  }

  public static TradingStatus trading() {
    return new TradingStatus(false, "");
  }
}
