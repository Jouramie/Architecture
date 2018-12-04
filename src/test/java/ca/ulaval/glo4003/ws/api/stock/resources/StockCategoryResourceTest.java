package ca.ulaval.glo4003.ws.api.stock.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.service.stock.StockService;
import com.google.common.collect.Lists;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockCategoryResourceTest {

  @Mock
  private StockService stockService;
  private StockCategoryResource stockCategoryResource;

  @Before
  public void setup() {
    stockCategoryResource = new StockCategoryResource(stockService);
  }

  @Test
  public void whenGetStocksCategories_thenGetCategories() {
    stockCategoryResource.getCategories();

    verify(stockService).getCategories();
  }

  @Test
  public void whenGetStocksCategories_thenReturnCategories() {
    List<String> expectedCategories = Lists.newArrayList("tech", "banking");
    given(stockService.getCategories()).willReturn(expectedCategories);

    List<String> resultingCategories = stockCategoryResource.getCategories();

    assertThat(resultingCategories).isSameAs(expectedCategories);
  }
}