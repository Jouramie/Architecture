package ca.ulaval.glo4003.domain.stock;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StockValueHistory {
  private final TreeMap<LocalDate, StockValue> values = new TreeMap<>();

  private static HistoricalStockValue getHistoricalStockValue(Map.Entry<LocalDate, StockValue> entry) {
    return new HistoricalStockValue(entry.getKey(), entry.getValue());
  }

  public void addValue(LocalDate date, StockValue value) {
    values.put(date, value);
  }

  public void addNextValue(StockValue value) {
    LocalDate nextDate = getLatestValue().date.plusDays(1);
    addValue(nextDate, value);
  }

  public HistoricalStockValue getLatestValue() {
    return getHistoricalStockValue(values.lastEntry());
  }

  public List<HistoricalStockValue> getAllStoredValues() {
    return values.entrySet().stream().map(StockValueHistory::getHistoricalStockValue).collect(toList());
  }

  public HistoricalStockValue getMaxValue(LocalDate from, LocalDate to) {
    return values.entrySet().stream()
        .filter((entry) -> !(entry.getKey().isBefore(from) || entry.getKey().isAfter(to)))
        .max(Comparator.comparing(firstEntry -> firstEntry.getValue().getMaximumValue().toUsd()))
        .map(StockValueHistory::getHistoricalStockValue).orElse(null);
  }
}
