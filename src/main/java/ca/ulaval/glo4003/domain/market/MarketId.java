package ca.ulaval.glo4003.domain.market;

public class MarketId {
  private final String id;

  public MarketId(String id) {
    this.id = id;
  }

  public String getValue() {
    return id;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof MarketId)) {
      return false;
    }

    MarketId otherId = (MarketId) other;
    return id.equalsIgnoreCase(otherId.id);
  }

  @Override
  public int hashCode() {
    return id.toUpperCase().hashCode();
  }
}
