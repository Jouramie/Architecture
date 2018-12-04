package ca.ulaval.glo4003.infrastructure.stock;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockHistory;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValue;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class StockCsvLoader {
  public static final LocalDate LAST_STOCK_DATA_DATE = LocalDate.of(2018, 11, 16);
  private static final String STOCKS_DATA_ZIP_PATH = "src/main/data/stocks_data.zip";
  private static final String STOCKS_FILE_PATH = "src/main/data/stocks.csv";

  private final StockRepository stockRepository;
  private final MarketRepository marketRepository;

  public StockCsvLoader(StockRepository stockRepository, MarketRepository marketRepository) {
    this.stockRepository = stockRepository;
    this.marketRepository = marketRepository;
  }

  public void load() throws IOException, MarketNotFoundException {
    Reader file = new FileReader(STOCKS_FILE_PATH);
    Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(file);
    for (CSVRecord record : records) {
      String title = record.get("title");
      String name = record.get("stock name");
      String category = record.get("category");
      MarketId marketId = new MarketId(record.get("market"));

      StockHistory history = getStockHistory(title, marketId);
      if (!history.getLatestHistoricalValue().date.isEqual(LAST_STOCK_DATA_DATE)) {
        continue;
      }

      Stock stock = new Stock(title, name, category, marketId, history);

      stockRepository.add(stock);
      marketRepository.findById(marketId).addStock(stock);
    }

    file.close();
  }

  private StockHistory getStockHistory(String title, MarketId marketId) throws IOException, MarketNotFoundException {
    StockHistory history = new StockHistory();
    Currency currency = marketRepository.findById(marketId).getCurrency();

    ZipFile zipFile = new ZipFile(STOCKS_DATA_ZIP_PATH);
    ZipEntry zipEntry = zipFile.getEntry(title + ".csv");
    InputStream fileStream = zipFile.getInputStream(zipEntry);

    Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader()
        .parse(new InputStreamReader(fileStream));

    for (CSVRecord record : records) {
      LocalDate date = LocalDate.parse(record.get("timestamp"));

      double openValue = Double.parseDouble(record.get("open"));
      double closeValue = Double.parseDouble(record.get("close"));
      double maximumValue = Double.parseDouble(record.get("high"));
      StockValue value = StockValue.create(
          new MoneyAmount(openValue, currency),
          new MoneyAmount(closeValue, currency),
          new MoneyAmount(maximumValue, currency));

      history.addValue(date, value);
    }

    fileStream.close();
    zipFile.close();

    return history;
  }
}
