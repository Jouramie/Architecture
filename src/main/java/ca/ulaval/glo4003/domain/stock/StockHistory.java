package ca.ulaval.glo4003.domain.stock;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class StockHistory {
  private final TreeMap<LocalDate, StockValue> values = new TreeMap<>();

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
    return values.entrySet().stream().map(this::getHistoricalStockValue).collect(toList());
  }

  public HistoricalStockValue getMaxValue(LocalDate from, LocalDate to) throws NoStockValueFitsCriteriaException {
    return getHistoricalValuesBetweenDates(from, to)
        .max(Comparator.comparing(entry -> entry.getValue().getMaximumValue().toUsd()))
        .map(this::getHistoricalStockValue).orElseThrow(NoStockValueFitsCriteriaException::new);
  }

  public StockValue getValueOnDay(LocalDate day) throws NoStockValueFitsCriteriaException {
    LocalDate currentDay = day;
    for (int i = 0; i < 10; ++i) {
      StockValue value = values.get(currentDay);
      if (value != null) {
        return value;
      }
      currentDay = currentDay.minusDays(1);
    }

    throw new NoStockValueFitsCriteriaException();
  }

  private Stream<Map.Entry<LocalDate, StockValue>> getHistoricalValuesBetweenDates(LocalDate from, LocalDate to) {
    return values.entrySet().stream().filter((entry) ->
        !(entry.getKey().isBefore(from) || entry.getKey().isAfter(to))
    );
  }

  private HistoricalStockValue getHistoricalStockValue(Map.Entry<LocalDate, StockValue> entry) {
    return new HistoricalStockValue(entry.getKey(), entry.getValue());
  }
}
