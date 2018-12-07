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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class StockCsvLoader {
  private static final String STOCKS_DATA_ZIP_FILENAME = "stocks_data.zip";
  private static final String STOCKS_FILE_FILENAME = "stocks.csv";

  private final StockRepository stockRepository;
  private final MarketRepository marketRepository;

  public StockCsvLoader(StockRepository stockRepository, MarketRepository marketRepository) {
    this.stockRepository = stockRepository;
    this.marketRepository = marketRepository;
  }

  public void load(Path basePath, LocalDate lastStockDataDate) throws IOException, MarketNotFoundException {
    ZipFile zipFile = new ZipFile(basePath.resolve(STOCKS_DATA_ZIP_FILENAME).toFile());

    Reader file = Files.newBufferedReader(basePath.resolve(STOCKS_FILE_FILENAME));
    Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(file);
    for (CSVRecord record : records) {
      String title = record.get("title");
      String name = record.get("stock name");
      String category = record.get("category");
      MarketId marketId = new MarketId(record.get("market"));

      StockHistory history = getStockHistory(zipFile, title, marketId);
      if (!history.getLatestHistoricalValue().date.isEqual(lastStockDataDate)) {
        continue;
      }

      Stock stock = new Stock(title, name, category, marketId, history);

      stockRepository.add(stock);
      marketRepository.findById(marketId).addStock(stock);
    }

    file.close();
    zipFile.close();
  }

  private StockHistory getStockHistory(ZipFile dataZipFile, String title, MarketId marketId) throws IOException, MarketNotFoundException {
    StockHistory history = new StockHistory();
    Currency currency = marketRepository.findById(marketId).getCurrency();

    ZipEntry zipEntry = dataZipFile.getEntry(title + ".csv");
    InputStream fileStream = dataZipFile.getInputStream(zipEntry);

    Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader()
        .parse(new InputStreamReader(fileStream));

    for (CSVRecord record : records) {
      LocalDate date = LocalDate.parse(record.get("timestamp"));

      double openValue = Double.parseDouble(record.get("open"));
      double closeValue = Double.parseDouble(record.get("close"));
      double maximumValue = Double.parseDouble(record.get("high"));
      StockValue value = new StockValue(
          new MoneyAmount(openValue, currency),
          new MoneyAmount(closeValue, currency),
          new MoneyAmount(maximumValue, currency));

      history.addValue(date, value);
    }

    fileStream.close();

    return history;
  }
}
