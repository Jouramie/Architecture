package ca.ulaval.glo4003.service.cart.assemblers;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.query.StockQuery;
import ca.ulaval.glo4003.domain.stock.query.StockQueryBuilder;
import ca.ulaval.glo4003.service.cart.dto.CartItemDto;
import ca.ulaval.glo4003.service.cart.exceptions.InvalidStockTitleException;
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
        stock.getName(), stock.getCategory(), stock.getValue().getLatestValue().toUsd(), quantity);
  }

  private Stock getStock(String title) {
    StockQuery stockQuery = new StockQueryBuilder().withTitle(title).build();
    List<Stock> stocks = stockRepository.find(stockQuery);
    if (stocks.isEmpty()) {
      throw new InvalidStockTitleException(title);
    }
    return stocks.get(0);
  }
}
