package ca.ulaval.glo4003.user;

import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import ca.ulaval.glo4003.ws.api.user.dto.StockLimitCreationDto;

public class StockLimitCreationRequestBuilder {
  public static final ApplicationPeriod DEFAULT_APPLICATION_PERIOD = ApplicationPeriod.DAILY;
  public static final int DEFAULT_STOCK_QUANTITY = 5;

  private int stockQuantity = DEFAULT_STOCK_QUANTITY;
  private ApplicationPeriod applicationPeriod = DEFAULT_APPLICATION_PERIOD;

  public StockLimitCreationRequestBuilder withApplicationPeriod(ApplicationPeriod applicationPeriod) {
    this.applicationPeriod = applicationPeriod;
    return this;
  }

  public StockLimitCreationRequestBuilder withStockQuantity(int stockQuantity) {
    this.stockQuantity = stockQuantity;
    return this;
  }

  public StockLimitCreationDto build() {
    return new StockLimitCreationDto(applicationPeriod, stockQuantity);
  }
}
