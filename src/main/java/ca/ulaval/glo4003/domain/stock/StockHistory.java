package ca.ulaval.glo4003.domain.stock;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

public class StockHistory {
  private final TreeMap<LocalDate, StockValue> values = new TreeMap<>();

  public void addValue(LocalDate date, StockValue value) {
    values.put(date, value);
  }

  public void addNextValue(StockValue value) {
    LocalDate nextDate = getLatestHistoricalValue().date.plusDays(1);
    addValue(nextDate, value);
  }

  public HistoricalStockValue getLatestHistoricalValue() {
    return toHistoricalStockValue(values.lastEntry());
  }

  public StockValue getLatestValue() {
    return values.lastEntry().getValue();
  }

  public List<HistoricalStockValue> getAllStoredValues() {
    return values.entrySet().stream().map(this::toHistoricalStockValue).collect(toList());
  }

  public Optional<HistoricalStockValue> getMaxValue(LocalDate from, LocalDate to) {
    return getHistoricalValuesBetweenDates(from, to)
        .max(Comparator.comparing(entry -> entry.getValue().getMaximumValue().toUsd()))
        .map(this::toHistoricalStockValue);
  }

  public Optional<StockValue> getValueOnDay(LocalDate day) {
    LocalDate currentDay = day;
    for (int i = 0; i < 10; ++i) {
      StockValue value = values.get(currentDay);
      if (value != null) {
        return Optional.of(value);
      }
      currentDay = currentDay.minusDays(1);
    }

    return Optional.empty();
  }

  public StockTrend getStockVariationTrendSinceDate(LocalDate date) {
    HistoricalStockValue latestValue = getLatestHistoricalValue();
    Optional<StockValue> valueOnDay = getValueOnDay(date);
    if (!valueOnDay.isPresent()) {
      return StockTrend.NO_DATA;
    }

    if (valueOnDay.get().getLatestValue().isGreaterThan(latestValue.value.getLatestValue())) {
      return StockTrend.DECREASING;
    } else if (valueOnDay.get().getLatestValue().isLessThan(latestValue.value.getLatestValue())) {
      return StockTrend.INCREASING;
    }

    return StockTrend.STABLE;
  }

  private Stream<Map.Entry<LocalDate, StockValue>> getHistoricalValuesBetweenDates(LocalDate from, LocalDate to) {
    return values.entrySet().stream().filter((entry) ->
        !(entry.getKey().isBefore(from) || entry.getKey().isAfter(to))
    );
  }

  private HistoricalStockValue toHistoricalStockValue(Map.Entry<LocalDate, StockValue> entry) {
    return new HistoricalStockValue(entry.getKey(), entry.getValue());
  }

  public void updateCurrentValue(BigDecimal variation) {
    StockValue newCurrentValue = values.lastEntry().getValue().updateCurrentValue(variation);
    values.put(values.lastKey(), newCurrentValue);
  }

  public boolean isAfterLatestValue(LocalDate date) {
    return date.isAfter(values.lastKey());
  }
}
