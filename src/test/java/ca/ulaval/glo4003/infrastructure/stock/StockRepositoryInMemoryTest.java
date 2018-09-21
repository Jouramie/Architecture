package ca.ulaval.glo4003.infrastructure.stock;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class StockRepositoryInMemoryTest {
    private StockRepositoryInMemory repository;
    private Stock SOME_STOCK = new Stock("STO1", "Stock 1");
    private Stock SOME_OTHER_STOCK = new Stock("STO2", "Stock 2");

    @Before
    public void setupStockRepository() {
        repository = new StockRepositoryInMemory();
        repository.add(SOME_STOCK);
        repository.add(SOME_OTHER_STOCK);
    }

    @Test
    public void whenGetByTitleAnExistingStock_thenStockIsReturned() {
        Stock result = repository.getByTitle(SOME_STOCK.getTitle());

        assertThat(result).isEqualTo(SOME_STOCK);
    }

    @Test
    public void whenGetByTitleANonExistingStock_thenStockNotFoundExceptionIsThrown() {
        assertThatExceptionOfType(StockNotFoundException.class).isThrownBy(() -> {
            repository.getByTitle("ASDF");
        });
    }

    @Test
    public void whenGetByNameAnExistingStock_thenStockIsReturned() {
        Stock result = repository.getByName(SOME_STOCK.getName());

        assertThat(result).isEqualTo(SOME_STOCK);
    }

    @Test
    public void whenGetByNameANonExistingStock_thenStockNotFoundExceptionIsThrown() {
        assertThatExceptionOfType(StockNotFoundException.class).isThrownBy(() -> {
            repository.getByName("ASDF");
        });
    }

    @Test
    public void whenGetAll_thenReturnAllStocks() {
        List<Stock> result = repository.getAll();

        assertThat(result).containsExactlyInAnyOrder(SOME_STOCK, SOME_OTHER_STOCK);
    }
}
