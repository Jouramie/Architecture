package ca.ulaval.glo4003.service.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;

import ca.ulaval.glo4003.domain.stock.HistoricalStockValue;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.service.stock.max.StockMaxResponseAssembler;
import ca.ulaval.glo4003.service.stock.max.StockMaxValueRetriever;
import ca.ulaval.glo4003.service.stock.max.StockMaxValueSinceParameter;
import ca.ulaval.glo4003.ws.api.stock.StockDto;
import ca.ulaval.glo4003.ws.api.stock.max.StockMaxResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockServiceTest {
  private static final String SOME_TITLE = "title";
  private static final String SOME_NAME = "name";
  private static final StockMaxValueSinceParameter SOME_PARAMETER = StockMaxValueSinceParameter.LAST_FIVE_DAYS;

  @Mock
  private StockRepository stockRepository;
  @Mock
  private StockAssembler stockAssembler;
  @Mock
  private StockMaxValueRetriever stockMaxValueRetriever;
  @Mock
  private StockMaxResponseAssembler stockMaxResponseAssembler;
  @Mock
  private Stock givenStock;
  @Mock
  private StockDto expectedDto;
  @Mock
  private HistoricalStockValue givenMaximumStockValue;
  @Mock
  private StockMaxResponseDto expectedMaxResponseDto;


  private StockService stockService;

  @Before
  public void setup() {
    stockService = new StockService(stockRepository, stockAssembler, stockMaxValueRetriever,
        stockMaxResponseAssembler);
  }

  @Test
  public void whenGetStockByTitle_thenStockIsGotFromRepository() throws StockNotFoundException {
    stockService.getStockByTitle(SOME_TITLE);

    verify(stockRepository).getByTitle(SOME_TITLE);
  }

  @Test
  public void whenGetStockByTitle_thenWeHaveCorrespondingDto() throws StockNotFoundException {
    given(stockRepository.getByTitle(SOME_TITLE)).willReturn(givenStock);
    given(stockAssembler.toDto(givenStock)).willReturn(expectedDto);

    StockDto resultingDto = stockService.getStockByTitle(SOME_TITLE);

    assertThat(resultingDto).isEqualTo(expectedDto);
  }

  @Test
  public void whenGetStockByName_thenStockIsGotFromRepository() throws StockNotFoundException {
    stockService.getStockByName(SOME_NAME);

    verify(stockRepository).getByName(SOME_NAME);
  }

  @Test
  public void whenGetStockByName_thenWeHaveCorrespondingDto() throws StockNotFoundException {
    given(stockRepository.getByName(SOME_NAME)).willReturn(givenStock);
    given(stockAssembler.toDto(givenStock)).willReturn(expectedDto);

    StockDto resultingDto = stockService.getStockByName(SOME_NAME);

    assertThat(resultingDto).isEqualTo(expectedDto);
  }

  @Test
  public void givenStockDoesNotExist_whenGettingStockByName_thenStockDoesNotExistExceptionIsThrown()
      throws StockNotFoundException {
    doThrow(StockNotFoundException.class).when(stockRepository).getByName(any());

    assertThatThrownBy(() -> stockService.getStockByName(SOME_NAME))
        .isInstanceOf(StockDoesNotExistException.class);
  }

  @Test
  public void givenStockNotFound_whenGettingStockByTitle_thenStockDoesNotExistExceptionIsThrown()
      throws StockNotFoundException {
    doThrow(StockNotFoundException.class).when(stockRepository).getByTitle(any());

    assertThatThrownBy(() -> stockService.getStockByTitle(SOME_NAME))
        .isInstanceOf(StockDoesNotExistException.class);
  }

  @Test
  public void whenGetStockMaxValue_thenWeHaveCorrespondingDto() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    given(stockRepository.getByTitle(SOME_TITLE)).willReturn(givenStock);
    given(stockMaxValueRetriever.getStockMaxValue(givenStock, SOME_PARAMETER)).willReturn(givenMaximumStockValue);
    given(stockMaxResponseAssembler.toDto(SOME_TITLE, givenMaximumStockValue)).willReturn(expectedMaxResponseDto);

    StockMaxResponseDto resultingDto = stockService.getStockMaxValue(SOME_TITLE, SOME_PARAMETER);

    assertThat(resultingDto).isEqualTo(expectedMaxResponseDto);
  }

  @Test
  public void givenStockDoesNotExist_whenGetStockMaxValue_thenStockDoesNotExistExceptionIsThrown()
      throws StockNotFoundException {
    doThrow(StockNotFoundException.class).when(stockRepository).getByTitle(any());

    assertThatThrownBy(() -> stockService.getStockMaxValue(SOME_TITLE, SOME_PARAMETER))
        .isInstanceOf(StockDoesNotExistException.class);
  }
}
