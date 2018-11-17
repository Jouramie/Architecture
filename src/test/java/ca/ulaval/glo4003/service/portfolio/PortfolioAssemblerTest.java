package ca.ulaval.glo4003.service.portfolio;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.portfolio.HistoricPortfolio;
import ca.ulaval.glo4003.domain.portfolio.InvalidStockInPortfolioException;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValue;
import ca.ulaval.glo4003.service.portfolio.dto.HistoricalPortfolioDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioHistoryDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioItemDto;
import ca.ulaval.glo4003.util.TestStockBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PortfolioAssemblerTest {
  private static final String SOME_TITLE = "title";
  private static final int SOME_QUANTITY = 45;
  private static final BigDecimal SOME_VALUE = new BigDecimal(23);
  private static final Currency SOME_CURRENCY = new Currency("CAD", new BigDecimal(12));
  private static final MoneyAmount SOME_CURRENT_MONEY_AMOUNT = new MoneyAmount(SOME_VALUE, SOME_CURRENCY);
  private static final StockValue SOME_CURRENT_STOCK_VALUE = new StockValue(SOME_CURRENT_MONEY_AMOUNT);
  private static final MoneyAmount SOME_HISTORICAL_MONEY_AMOUNT = new MoneyAmount(66.66, Currency.USD);
  private static final StockValue SOME_HISTORICAL_STOCK_VALUE = new StockValue(SOME_HISTORICAL_MONEY_AMOUNT);
  private static final MoneyAmount SOME_PORTFOLIO_TOTAL = new MoneyAmount(SOME_CURRENT_MONEY_AMOUNT.toUsd()
      .multiply(new BigDecimal(SOME_QUANTITY)), Currency.USD);
  private static final LocalDate SOME_PREVIOUS_DATE = LocalDate.now().minusDays(5);
  private static final LocalDate SOME_DATE = LocalDate.now();

  private StockCollection someStockCollection;
  private Stock someStock;
  private HistoricPortfolio someFirstPortfolio;
  private HistoricPortfolio someSecondPortfolio;

  @Mock
  private Portfolio somePortfolio;
  @Mock
  private StockRepository someStockRepository;

  private PortfolioAssembler portfolioAssembler;

  @Before
  public void setupPortfolioAssembler() throws StockNotFoundException, InvalidStockInPortfolioException, NoStockValueFitsCriteriaException {
    portfolioAssembler = new PortfolioAssembler(someStockRepository);

    given(someStockRepository.exists(SOME_TITLE)).willReturn(true);
    someStockCollection = new StockCollection().add(SOME_TITLE, SOME_QUANTITY, someStockRepository);
    given(somePortfolio.getStocks()).willReturn(someStockCollection);
    given(somePortfolio.getQuantity(SOME_TITLE)).willReturn(SOME_QUANTITY);
    someStock = new TestStockBuilder()
        .withTitle(SOME_TITLE)
        .withHistoricalValue(SOME_PREVIOUS_DATE.minusDays(1), SOME_HISTORICAL_STOCK_VALUE)
        .withHistoricalValue(SOME_PREVIOUS_DATE, SOME_HISTORICAL_STOCK_VALUE)
        .withCloseValue(SOME_CURRENT_MONEY_AMOUNT)
        .build();
    given(someStockRepository.findByTitle(SOME_TITLE)).willReturn(someStock);

    given(somePortfolio.getCurrentTotalValue(someStockRepository)).willReturn(SOME_PORTFOLIO_TOTAL);

    HistoricPortfolio firstPortfolio = new HistoricPortfolio(SOME_PREVIOUS_DATE, someStockCollection);
    someFirstPortfolio = spy(firstPortfolio);
    doReturn(SOME_PORTFOLIO_TOTAL).when(someFirstPortfolio).getTotal(someStockRepository);
    HistoricPortfolio secondPortfolio = new HistoricPortfolio(SOME_PREVIOUS_DATE.minusDays(1), someStockCollection);
    someSecondPortfolio = spy(secondPortfolio);
    doReturn(SOME_PORTFOLIO_TOTAL).when(someSecondPortfolio).getTotal(someStockRepository);
  }

  @Test
  public void givenEmptyPortfolio_whenToDto_thenPortfolioItemListIsEmpty()
      throws InvalidStockInPortfolioException {
    StockCollection stockCollection = new StockCollection();
    given(somePortfolio.getStocks()).willReturn(stockCollection);

    PortfolioDto responseDto = portfolioAssembler.toDto(somePortfolio);

    assertThat(responseDto.stocks).isEmpty();
  }

  @Test
  public void givenPortfolioNotEmpty_whenToDto_thenItemsAreAllPresent()
      throws InvalidStockInPortfolioException {
    PortfolioDto responseDto = portfolioAssembler.toDto(somePortfolio);

    int numberOfStocks = somePortfolio.getStocks().getTitles().size();
    assertThat(responseDto.stocks).hasSize(numberOfStocks);
  }

  @Test
  public void whenToDto_thenPortfolioIsMappedCorrectly()
      throws InvalidStockInPortfolioException {
    PortfolioDto responseDto = portfolioAssembler.toDto(somePortfolio);

    assertThatDtoIsMappedCorrectly(responseDto);
  }

  @Test
  public void givenNoHistoricalPortfolio_whenToDto_thenDtoWithEmptyList() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    PortfolioHistoryDto dto = portfolioAssembler.toDto(new TreeSet<>());

    assertThat(dto.historicalPortfolios).hasSize(0);
  }

  @Test
  public void givenHistoricalPortfolioWithMultipleDates_whenToDto_thenDtoContainsAPortfolioForEachDay() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    TreeSet<HistoricPortfolio> portfolios = new TreeSet<>();
    portfolios.add(someFirstPortfolio);
    portfolios.add(someSecondPortfolio);

    PortfolioHistoryDto dto = portfolioAssembler.toDto(portfolios);

    List<LocalDate> dates = dto.historicalPortfolios.stream().map((portfolio) -> portfolio.date).collect(toList());
    assertThat(dates).containsExactly(SOME_PREVIOUS_DATE.minusDays(1), SOME_PREVIOUS_DATE);
  }

  @Test
  public void givenHistoricalPortfolioWithStocks_whenToDto_thenTotalValueIsCalculated() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    TreeSet<HistoricPortfolio> portfolios = new TreeSet<>();
    portfolios.add(someFirstPortfolio);

    PortfolioHistoryDto dto = portfolioAssembler.toDto(portfolios);

    HistoricalPortfolioDto portfolioDto = dto.historicalPortfolios.get(0);
    assertThat(portfolioDto.totalValue).isEqualTo(SOME_PORTFOLIO_TOTAL.toUsd());
  }

  @Test
  public void givenHistoricalPortfolioWithStocks_whenToDto_thenStockHistoricalValueIsUsed() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    TreeSet<HistoricPortfolio> portfolios = new TreeSet<>();
    portfolios.add(someFirstPortfolio);

    PortfolioHistoryDto dto = portfolioAssembler.toDto(portfolios);

    HistoricalPortfolioDto portfolioDto = dto.historicalPortfolios.get(0);
    PortfolioItemDto portfolioItemDto = portfolioDto.stocks.get(0);
    assertThat(portfolioItemDto.title).isEqualTo(SOME_TITLE);
    assertThat(portfolioItemDto.quantity).isEqualTo(SOME_QUANTITY);
    assertThat(portfolioItemDto.currentValue).isEqualTo(SOME_HISTORICAL_STOCK_VALUE.getCurrentValue().toUsd());
  }

  private void assertThatDtoIsMappedCorrectly(PortfolioDto dto) {
    assertThat(dto.currentTotalValue).isEqualTo(SOME_PORTFOLIO_TOTAL.toUsd());
    for (PortfolioItemDto itemDto : dto.stocks) {
      assertThatItemDtosAreMappedCorrectly(itemDto);
    }
  }

  private void assertThatItemDtosAreMappedCorrectly(PortfolioItemDto dto) {
    assertThat(dto.title).isEqualTo(SOME_TITLE);
    assertThat(dto.quantity).isEqualTo(SOME_QUANTITY);
    assertThat(dto.currentValue).isEqualTo(SOME_CURRENT_MONEY_AMOUNT.toUsd());
  }
}
