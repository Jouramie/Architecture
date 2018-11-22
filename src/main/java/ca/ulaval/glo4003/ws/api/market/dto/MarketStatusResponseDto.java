package ca.ulaval.glo4003.ws.api.market.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MarketStatusResponseDto {

  public final String market;
  public final String status;
  public final String haltMessage;

  public MarketStatusResponseDto(String market, String status, String haltMessage) {
    this.market = market;
    this.status = status;
    this.haltMessage = haltMessage;
  }
}
