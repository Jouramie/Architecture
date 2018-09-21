package ca.ulaval.glo4003.infrastructure.stock;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StockRepositoryInMemory implements StockRepository {
    private Map<String, Stock> stocks = new HashMap<>();

    @Override
    public List<Stock> getAll() {
        return stocks.values().stream().collect(Collectors.toList());
    }

    @Override
    public Stock getByTitle(String title) {
        Stock result = stocks.get(title);
        if(result == null) {
            throw new StockNotFoundException("Cannot find stock with title " + title);
        }

        return result;
    }

    @Override
    public Stock getByName(String name) {
        return stocks.values().stream().filter((stock) -> stock.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new StockNotFoundException("Cannot find stock with name " + name));
    }

    @Override
    public void add(Stock stock) {
        stocks.put(stock.getTitle(), stock);
    }
}
