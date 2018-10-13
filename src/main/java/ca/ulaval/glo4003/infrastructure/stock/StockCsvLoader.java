package ca.ulaval.glo4003.infrastructure.stock;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValue;
import ca.ulaval.glo4003.domain.stock.StockValueHistory;
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
  private static final String STOCKS_DATA_ZIP_PATH = "src/main/data/stocks_data.zip";
  private static final String STOCKS_FILE_PATH = "src/main/data/stocks.csv";

  private final StockRepository stockRepository;
  private final MarketRepository marketRepository;

  public StockCsvLoader(StockRepository stockRepository, MarketRepository marketRepository) {
    this.stockRepository = stockRepository;
    this.marketRepository = marketRepository;
  }

  public LocalDate load() throws IOException, MarketNotFoundException {
    LocalDate startDate = LocalDate.now();

    Reader file = new FileReader(STOCKS_FILE_PATH);
    Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(file);
    for (CSVRecord record : records) {
      String title = record.get("title");
      String name = record.get("stock name");
      String category = record.get("category");
      MarketId marketId = new MarketId(record.get("market"));

      Stock stock = new Stock(title, name, category, marketId, getValueHistory(title, marketId));
      stockRepository.add(stock);
      startDate = stock.getValueHistory().getLatestValue().date;
    }

    file.close();

    return startDate.plusDays(1);
  }

  private StockValueHistory getValueHistory(String title, MarketId marketId) throws IOException, MarketNotFoundException {
    StockValueHistory history = new StockValueHistory();
    Currency currency = marketRepository.getById(marketId).getCurrency();

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
      StockValue value = new StockValue(new MoneyAmount(openValue, currency),
          new MoneyAmount(closeValue, currency), new MoneyAmount(maximumValue, currency));

      history.addValue(date, value);
    }

    fileStream.close();
    zipFile.close();

    return history;
  }
}
