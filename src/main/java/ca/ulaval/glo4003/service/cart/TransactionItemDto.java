package ca.ulaval.glo4003.service.cart;

import java.math.BigDecimal;

public class TransactionItemDto {


  public final String title;

  public final int quantity;

  public final BigDecimal moneyAmount;

  public final String currency;

  public TransactionItemDto(String title, int quantity, BigDecimal moneyAmount, String currency) {
    this.title = title;
    this.quantity = quantity;
    this.moneyAmount = moneyAmount;
    this.currency = currency;
  }
}
