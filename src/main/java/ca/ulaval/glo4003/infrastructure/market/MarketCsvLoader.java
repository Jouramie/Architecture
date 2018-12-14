package ca.ulaval.glo4003.infrastructure.market;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.state.ClosedMarketState;
import ca.ulaval.glo4003.domain.market.state.Market;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class MarketCsvLoader {
  private static final String EXCHANGE_RATES_FILENAME = "exchange_rates.csv";
  private static final String MARKETS_FILE_FILENAME = "markets.csv";

  private final MarketRepository marketRepository;
  private final StockRepository stockRepository;

  public MarketCsvLoader(MarketRepository marketRepository, StockRepository stockRepository) {
    this.marketRepository = marketRepository;
    this.stockRepository = stockRepository;
  }

  public void load(Path basePath) throws IOException {
    Map<MarketId, Currency> currency = loadExchangeRates(basePath);

    Reader file = Files.newBufferedReader(basePath.resolve(MARKETS_FILE_FILENAME));
    Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(file);
    for (CSVRecord record : records) {
      MarketId marketId = new MarketId(record.get("market"));
      LocalTime open = parseTime(record.get("open"));
      LocalTime close = parseTime(record.get("close"));
      boolean halt = Boolean.parseBoolean(record.get("tradinghalt"));

      Market market = new Market(marketId, open, close, currency.get(marketId), new ArrayList<>(), new ClosedMarketState());
      if (halt) {
        market.halt("");
      }

      marketRepository.add(market);
    }

    file.close();
  }

  private Map<MarketId, Currency> loadExchangeRates(Path basePath) throws IOException {
    Map<MarketId, Currency> exchangeRates = new HashMap<>();

    Reader file = Files.newBufferedReader(basePath.resolve(EXCHANGE_RATES_FILENAME));
    Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(file);
    CSVParser.parse(file, CSVFormat.EXCEL);
    for (CSVRecord record : records) {
      String market = record.get("Market");
      BigDecimal rate = new BigDecimal(record.get("convert_rate_to_USD"));
      String currencyName = record.get("Currency");

      exchangeRates.put(new MarketId(market), new Currency(currencyName, rate));
    }

    file.close();

    return exchangeRates;
  }

  private LocalTime parseTime(String string) {
    return LocalTime.parse(string, DateTimeFormatter.ISO_OFFSET_TIME);
  }
}
