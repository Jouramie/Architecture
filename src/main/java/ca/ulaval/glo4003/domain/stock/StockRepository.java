package ca.ulaval.glo4003.domain.stock;

import java.util.List;

public interface StockRepository {
    List<Stock> getAll();
    Stock getByTitle(String title);
    Stock getByName(String name);

    void add(Stock stock);
}
