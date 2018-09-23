package ca.ulaval.glo4003.domain.market;

public class MarketId {
    private final String id;

    public MarketId(String id) {
        this.id = id;
    }

    public String getValue() {
        return this.id;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if ( !(other instanceof MarketId) ) return false;

        final MarketId otherId = (MarketId)other;
        return this.id.equals(otherId.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
