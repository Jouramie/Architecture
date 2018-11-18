package ca.ulaval.glo4003.ws.api.market.dto;

public class MarketStatusResponseDto {

  public final String market;
  public final String status;

  public MarketStatusResponseDto(String market, String status) {
    this.market = market;
    this.status = status;
  }
}
