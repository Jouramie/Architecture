package ca.ulaval.glo4003.ws.api.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(name = "Transaction item")
public class ApiTransactionItemDto {

  public final String title;
  public final int quantity;
  public final BigDecimal moneyAmount;
  public final String currency;

  public ApiTransactionItemDto(String title, int quantity, BigDecimal moneyAmount, String currency) {
    this.title = title;
    this.quantity = quantity;
    this.moneyAmount = moneyAmount;
    this.currency = currency;
  }
}
