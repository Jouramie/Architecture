package ca.ulaval.glo4003.service.cart;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.util.List;
import javax.inject.Inject;

@Component
public class CartItemAssembler {
  private final StockRepository stockRepository;

  @Inject
  public CartItemAssembler(StockRepository stockRepository) {
    this.stockRepository = stockRepository;
  }

  public List<CartItemDto> toDtoList(StockCollection items) {
    return items.getTitles().stream()
        .map((title) -> toDto(title, items.getQuantity(title)))
        .collect(toList());
  }

  public CartItemDto toDto(String title, int quantity) {
    Stock stock = getStock(title);

    return new CartItemDto(stock.getTitle(), stock.getMarketId().getValue(),
        stock.getName(), stock.getCategory(), stock.getValue().getCurrentValue().toUsd(), quantity);
  }

  private Stock getStock(String title) {
    try {
      return stockRepository.findByTitle(title);
    } catch (StockNotFoundException exception) {
      throw new InvalidStockTitleException(exception);
    }
  }
}
