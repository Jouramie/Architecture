package ca.ulaval.glo4003.service.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.ws.api.stock.StockDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockServiceTest {
  private static final String SOME_TITLE = "title";
  private static final String SOME_NAME = "name";
  @Mock
  private StockRepository stockRepository;
  @Mock
  private StockAssembler stockAssembler;
  @Mock
  private Stock givenStock;
  @Mock
  private StockDto expectedDto;
  private StockService stockService;

  @Before
  public void setup() {
    stockService = new StockService(stockRepository, stockAssembler);
  }

  @Test
  public void whenGetStockByTitle_thenStockIsGotFromRepository() {
    stockService.getStockByTitle(SOME_TITLE);

    verify(stockRepository).getByTitle(SOME_TITLE);
  }

  @Test
  public void whenGetStockByTitle_thenWeHaveCorrespondingDto() {
    given(stockRepository.getByTitle(SOME_TITLE)).willReturn(givenStock);
    given(stockAssembler.toDto(givenStock)).willReturn(expectedDto);

    StockDto resultingDto = stockService.getStockByTitle(SOME_TITLE);

    assertThat(resultingDto).isEqualTo(expectedDto);
  }

  @Test
  public void whenGetStockByName_thenStockIsGotFromRepository() {
    stockService.getStockByName(SOME_NAME);

    verify(stockRepository).getByName(SOME_NAME);
  }

  @Test
  public void whenGetStockByName_thenWeHaveCorrespondingDto() {
    given(stockRepository.getByName(SOME_NAME)).willReturn(givenStock);
    given(stockAssembler.toDto(givenStock)).willReturn(expectedDto);

    StockDto resultingDto = stockService.getStockByName(SOME_NAME);

    assertThat(resultingDto).isEqualTo(expectedDto);
  }
}
