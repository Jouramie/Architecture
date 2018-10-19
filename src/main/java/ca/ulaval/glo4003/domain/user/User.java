package ca.ulaval.glo4003.domain.user;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.util.List;

public class User {
  private final String email;
  private final String password;
  private final UserRole role;
  private final Cart cart;
  private final Portfolio portfolio;

  public User(String email, String password, UserRole role, StockRepository stockRepository) {
    this.email = email;
    this.password = password;
    this.role = role;
    cart = new Cart();
    portfolio = new Portfolio(stockRepository);
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public UserRole getRole() {
    return role;
  }

  public boolean isThisYourPassword(String password) {
    return this.password.equals(password);
  }

  public Cart getCart() {
    return cart;
  }

  public void acquireStock(String title, int quantity) throws StockNotFoundException {
    portfolio.add(title, quantity);
  }

  public List<String> getStocks() {
    return portfolio.getStocks().getTitles();
  }
}
