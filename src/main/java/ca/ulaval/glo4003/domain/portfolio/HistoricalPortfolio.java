package ca.ulaval.glo4003.domain.portfolio;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.time.LocalDate;

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
      Stock stock = stockRepository.findByTitle(title);
      MoneyAmount stockValue = stock.getLatestValueOnDate(date).getLatestValue();
      currentTotal = currentTotal.add(stockValue.multiply(stocks.getQuantity(title)));
    }

    return currentTotal;
  }

  @Override
  public int compareTo(HistoricalPortfolio other) {
    return date.compareTo(other.date);
  }
}
