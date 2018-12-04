package ca.ulaval.glo4003.domain.portfolio;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.query.StockQuery;
import ca.ulaval.glo4003.domain.stock.query.StockQueryBuilder;
import java.time.LocalDate;
import java.util.List;

public class HistoricalPortfolio implements Comparable<HistoricalPortfolio> {
  public final LocalDate date;
  public final StockCollection stocks;

  public HistoricalPortfolio(LocalDate date, StockCollection stocks) {
    this.date = date;
    this.stocks = stocks;
  }

  public MoneyAmount getTotal(StockRepository stockRepository) throws StockNotFoundException, NoStockValueFitsCriteriaException {
    MoneyAmount currentTotal = MoneyAmount.zero(Currency.USD);
    for (String title : stocks.getTitles()) {
      Stock stock = getStock(stockRepository, title);
      MoneyAmount stockValue = stock.getLatestValueOnDate(date).getLatestValue();
      currentTotal = currentTotal.add(stockValue.multiply(stocks.getQuantity(title)));
    }

    return currentTotal;
  }

  private Stock getStock(StockRepository stockRepository, String title) throws StockNotFoundException {
    StockQuery stockQuery = new StockQueryBuilder().withTitle(title).build();
    List<Stock> stocks = stockRepository.find(stockQuery);
    if (stocks.isEmpty()) {
      throw new StockNotFoundException(title);
    }
    return stocks.get(0);
  }

  @Override
  public int compareTo(HistoricalPortfolio other) {
    return date.compareTo(other.date);
  }
}
