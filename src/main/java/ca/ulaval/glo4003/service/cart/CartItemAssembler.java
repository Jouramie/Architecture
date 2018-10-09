package ca.ulaval.glo4003.service.cart;

import ca.ulaval.glo4003.domain.cart.CartItem;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.infrastructure.injection.Component;
import ca.ulaval.glo4003.ws.api.cart.CartItemResponseDto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;

@Component
public class CartItemAssembler {
  private final StockRepository stockRepository;

  @Inject
  public CartItemAssembler(StockRepository stockRepository) {
    this.stockRepository = stockRepository;
  }

  public CartItemResponseDto toDto(CartItem item) throws StockNotFoundException {
    Stock stock = stockRepository.getByTitle(item.title);

    return new CartItemResponseDto(stock.getTitle(), stock.getMarketId().getValue(),
        stock.getName(), stock.getCategory(), stock.getValue().getCurrentValue().toUsd(),
        item.quantity);
  }

  public List<CartItemResponseDto> toDtoList(Collection<CartItem> items)
      throws StockNotFoundException {
    List<CartItemResponseDto> cartItemResponses = new ArrayList<>();
    for (CartItem item: items) {
      cartItemResponses.add(toDto(item));
    }
    return cartItemResponses;
  }
}
