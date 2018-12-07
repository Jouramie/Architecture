package ca.ulaval.glo4003.infrastructure.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class StocksDataSettings {
  public static final Path STOCKS_DATA_PATH = Paths.get("src", "main", "data");
  public static final LocalDate LAST_STOCK_DATA_DATE = LocalDate.of(2018, 11, 23);
}
