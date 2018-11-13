package ca.ulaval.glo4003.infrastructure.market;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.states.ClosedMarketState;
import ca.ulaval.glo4003.domain.market.states.Market;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class MarketCsvLoader {
  private static final String EXCHANGE_RATES_PATH = "src/main/data/exchange_rates.csv";
  private static final String MARKETS_FILE_PATH = "src/main/data/markets.csv";

  private final MarketRepository marketRepository;
  private final StockRepository stockRepository;
  private final StockValueRetriever stockValueRetriever;

  public MarketCsvLoader(MarketRepository marketRepository, StockRepository stockRepository,
                         StockValueRetriever stockValueRetriever) {
    this.marketRepository = marketRepository;
    this.stockRepository = stockRepository;
    this.stockValueRetriever = stockValueRetriever;
  }

  public void load() throws IOException {
    Map<MarketId, Currency> currency = loadExchangeRates();

    Reader file = new FileReader(MARKETS_FILE_PATH);
    Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(file);
    for (CSVRecord record : records) {
      MarketId marketId = new MarketId(record.get("market"));
      LocalTime open = parseTime(record.get("saveOpeningPrice"));
      LocalTime close = parseTime(record.get("saveClosingPrice"));
      boolean halt = Boolean.parseBoolean(record.get("tradinghalt"));
      List<Stock> stocks = stockRepository.findByMarket(marketId);

      Market market = new Market(marketId, open, close, currency.get(marketId), stocks, new ClosedMarketState());
      if (halt) {
        market.halt();
      }

      marketRepository.add(market);
    }

    file.close();
  }

  private Map<MarketId, Currency> loadExchangeRates() throws IOException {
    Map<MarketId, Currency> exchangeRates = new HashMap<>();

    Reader file = new FileReader(EXCHANGE_RATES_PATH);
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
