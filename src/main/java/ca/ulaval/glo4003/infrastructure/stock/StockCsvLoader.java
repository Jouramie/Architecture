package ca.ulaval.glo4003.infrastructure.stock;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class StockCsvLoader {
  // TODO: Read the currencies from the CSV
  private static final Currency DEFAULT_CURRENCY = new Currency("CAD", new BigDecimal(0.77));
  // TODO: Base start amount on latest historic value
  private static final MoneyAmount DEFAULT_START_AMOUNT = new MoneyAmount(100.00, DEFAULT_CURRENCY);
  private static final String STOCKS_FILE_PATH = "src/main/data/stocks.csv";
  
  private final StockRepository stockRepository;

  StockCsvLoader(StockRepository stockRepository) {
    this.stockRepository = stockRepository;
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

      Stock stock = new Stock(title, name, category, marketId, DEFAULT_START_AMOUNT);
      stockRepository.add(stock);
    }
  }
}
