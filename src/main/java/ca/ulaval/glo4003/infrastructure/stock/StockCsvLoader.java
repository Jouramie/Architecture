package ca.ulaval.glo4003.infrastructure.stock;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class StockCsvLoader {
  // TODO: Base start amount on latest historic value
  private static final double DEFAULT_START_AMOUNT = 100.00;
  private static final String STOCKS_FILE_PATH = "src/main/data/stocks.csv";

  private final StockRepository stockRepository;
  private final MarketRepository marketRepository;

  StockCsvLoader(StockRepository stockRepository, MarketRepository marketRepository) {
    this.stockRepository = stockRepository;
    this.marketRepository = marketRepository;
  }

  public void load() throws IOException {
    Reader file = new FileReader(STOCKS_FILE_PATH);
    Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(file);
    CSVParser.parse(file, CSVFormat.EXCEL);
    for (CSVRecord record : records) {
      String title = record.get("title");
      String name = record.get("stock name");
      String category = record.get("category");
      MarketId marketId = new MarketId(record.get("market"));

      MoneyAmount startAmount = getStartValue(marketId);

      Stock stock = new Stock(title, name, category, marketId, startAmount);
      stockRepository.add(stock);
    }
  }

  private MoneyAmount getStartValue(MarketId marketId) {
    Currency currency = this.marketRepository.getById(marketId).getCurrency();

    return new MoneyAmount(DEFAULT_START_AMOUNT, currency);
  }
}
