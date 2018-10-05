package ca.ulaval.glo4003.infrastructure.stock;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.javatuples.Pair;

public class StockCsvLoader {
  private static final String STOCKS_DATA_ZIP_PATH = "src/main/data/stocks_data.zip";
  private static final String STOCKS_FILE_PATH = "src/main/data/stocks.csv";

  private final StockRepository stockRepository;
  private final MarketRepository marketRepository;

  public StockCsvLoader(StockRepository stockRepository, MarketRepository marketRepository) {
    this.stockRepository = stockRepository;
    this.marketRepository = marketRepository;
  }

  public void load() throws IOException {
    Reader file = new FileReader(STOCKS_FILE_PATH);
    Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(file);
    for (CSVRecord record : records) {
      String title = record.get("title");
      String name = record.get("stock name");
      String category = record.get("category");
      MarketId marketId = new MarketId(record.get("market"));

      Pair<MoneyAmount, MoneyAmount> lastValues = getLastValues(title, marketId);

      Stock stock = new Stock(title, name, category, marketId, lastValues.getValue0(),
          lastValues.getValue1());
      stockRepository.add(stock);
    }

    file.close();
  }

  private Pair<MoneyAmount, MoneyAmount> getLastValues(String title, MarketId marketId)
      throws IOException {
    ZipFile zipFile = new ZipFile(STOCKS_DATA_ZIP_PATH);
    ZipEntry zipEntry = zipFile.getEntry(title + ".csv");
    InputStream fileStream = zipFile.getInputStream(zipEntry);

    Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader()
        .parse(new InputStreamReader(fileStream));
    CSVRecord firstRecord = records.iterator().next();
    double openValue = Double.parseDouble(firstRecord.get("open"));
    double closeValue = Double.parseDouble(firstRecord.get("close"));

    fileStream.close();
    zipFile.close();

    Currency currency = marketRepository.getById(marketId).getCurrency();
    return new Pair<>(new MoneyAmount(openValue, currency), new MoneyAmount(closeValue, currency));
  }
}
