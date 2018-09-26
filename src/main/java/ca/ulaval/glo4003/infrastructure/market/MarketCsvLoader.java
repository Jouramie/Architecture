package ca.ulaval.glo4003.infrastructure.market;

import ca.ulaval.glo4003.domain.market.Market;
import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class MarketCsvLoader {
  private final MarketRepository marketRepository;
  private final StockRepository stockRepository;
  private final StockValueRetriever stockValueRetriever;

  private final String EXCHANGE_RATHS_PATH = "src/main/data/exchange_rates.csv";
  private final String MARKETS_FILE_PATH = "src/main/data/markets.csv";

  MarketCsvLoader(MarketRepository marketRepository, StockRepository stockRepository, StockValueRetriever stockValueRetriever) {
    this.marketRepository = marketRepository;
    this.stockRepository = stockRepository;
    this.stockValueRetriever = stockValueRetriever;
  }

  public void load() throws IOException {
    Map<String, Currency> exchangeRates = loadExchangeRates();

    Reader file = new FileReader(MARKETS_FILE_PATH);
    Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(file);
    CSVParser.parse(file, CSVFormat.EXCEL);
    for (CSVRecord record : records) {
      String id = record.get("market");
      LocalTime open = parseTime(record.get("open"));
      LocalTime close = parseTime(record.get("close"));
      boolean halt = Boolean.parseBoolean(record.get("tradinghalt"));

      Market market = new Market(new MarketId(id), open, close, stockRepository, stockValueRetriever);
      if (halt) {
        market.halt();
      }

      marketRepository.add(market);
    }
  }

  private Map<String, Currency> loadExchangeRates() throws IOException {
    Map<String, Currency> exchangeRates = new HashMap<>();

    Reader file = new FileReader(EXCHANGE_RATHS_PATH);
    Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(file);
    CSVParser.parse(file, CSVFormat.EXCEL);
    for (CSVRecord record : records) {
      String market = record.get("Market");
      BigDecimal rate = new BigDecimal(record.get("convert_rate_to_USD"));
      String currencyName = record.get("Currency");

      exchangeRates.put(market, new Currency(currencyName, rate));
    }

    return exchangeRates;
  }

  private LocalTime parseTime(String string) {
    return LocalTime.parse(string, DateTimeFormatter.ISO_OFFSET_TIME);
  }
}
