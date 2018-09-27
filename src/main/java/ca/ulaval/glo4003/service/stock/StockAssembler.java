package ca.ulaval.glo4003.service.stock;

import ca.ulaval.glo4003.domain.stock.Stock;

class StockAssembler {

  public StockDto toDto(Stock stock) {

    return new StockDto(
        stock.getTitle(),
        stock.getName(),
        stock.getCategory(),
        stock.getMarketId().getValue(),
        stock.getValue().getOpenValue().toUsd().doubleValue(),
        stock.getValue().getCurrentValue().toUsd().doubleValue(),
        stock.getValue().getCloseValue().toUsd().doubleValue());
  }
}
