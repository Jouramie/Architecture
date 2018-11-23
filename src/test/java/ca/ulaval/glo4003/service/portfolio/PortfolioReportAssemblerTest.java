package ca.ulaval.glo4003.service.portfolio;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.portfolio.HistoricalPortfolio;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValue;
import ca.ulaval.glo4003.service.portfolio.dto.HistoricalPortfolioDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioItemDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioReportDto;
import ca.ulaval.glo4003.util.TestStockBuilder;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PortfolioReportAssemblerTest {
  private static final String SOME_TITLE = "title";
  private static final String SOME_INCREASING_STOCK_TITLE = "increasing";
  private static final String SOME_DECREASING_STOCK_TITLE = "decreasing";
  private static final int SOME_QUANTITY = 45;
  private static final MoneyAmount SOME_FIRST_HISTORICAL_MONEY_AMOUNT = new MoneyAmount(66.66, Currency.USD);
  private static final StockValue SOME_FIRST_HISTORICAL_STOCK_VALUE = new StockValue(SOME_FIRST_HISTORICAL_MONEY_AMOUNT);
  private static final MoneyAmount SOME_SECOND_HISTORICAL_MONEY_AMOUNT = new MoneyAmount(77.77, Currency.USD);
  private static final StockValue SOME_SECOND_HISTORICAL_STOCK_VALUE = new StockValue(SOME_SECOND_HISTORICAL_MONEY_AMOUNT);
  private static final MoneyAmount SOME_FIRST_PORTFOLIO_TOTAL = SOME_FIRST_HISTORICAL_MONEY_AMOUNT.multiply(SOME_QUANTITY);
  private static final LocalDate SOME_FIRST_DATE = LocalDate.now().minusDays(5);
  private static final LocalDate SOME_SECOND_DATE = SOME_FIRST_DATE.plusDays(1);

  private StockCollection someStockCollection;
  private Stock someStock;
  @Mock
  private StockRepository someStockRepository;
  @Mock
  private HistoricalPortfolio someFirstPortfolio;
  @Mock
  private HistoricalPortfolio someSecondPortfolio;

  private PortfolioReportAssembler assembler;

  @Before
  public void setupPortfolioReportAssembler() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    assembler = new PortfolioReportAssembler(someStockRepository);
    setupStockRepository();
    setupHistoricalPortfolios();
  }

  private void setupStockRepository() throws StockNotFoundException {
    given(someStockRepository.exists(SOME_TITLE)).willReturn(true);
    someStockCollection = new StockCollection().add(SOME_TITLE, SOME_QUANTITY, someStockRepository);
    someStock = new TestStockBuilder()
        .withTitle(SOME_TITLE)
        .withHistoricalValue(SOME_FIRST_DATE, SOME_FIRST_HISTORICAL_STOCK_VALUE)
        .withHistoricalValue(SOME_SECOND_DATE, SOME_SECOND_HISTORICAL_STOCK_VALUE)
        .build();
    given(someStockRepository.findByTitle(SOME_TITLE)).willReturn(someStock);
  }

  private void setupHistoricalPortfolios() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    someFirstPortfolio = spy(new HistoricalPortfolio(SOME_FIRST_DATE, someStockCollection));
    doReturn(SOME_FIRST_PORTFOLIO_TOTAL).when(someFirstPortfolio).getTotal(someStockRepository);

    someSecondPortfolio = new HistoricalPortfolio(SOME_SECOND_DATE, someStockCollection);
  }

  @Test
  public void givenNoHistoricalPortfolio_whenToDto_thenDtoWithEmptyList() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    PortfolioReportDto dto = assembler.toDto(new TreeSet<>(), SOME_INCREASING_STOCK_TITLE, SOME_DECREASING_STOCK_TITLE);

    assertThat(dto.history).hasSize(0);
  }

  @Test
  public void givenHistoricalPortfolioWithMultipleDates_whenToDto_thenDtoContainsAPortfolioForEachDay() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    TreeSet<HistoricalPortfolio> portfolios = new TreeSet<>();
    portfolios.add(someFirstPortfolio);
    portfolios.add(someSecondPortfolio);

    PortfolioReportDto dto = assembler.toDto(portfolios, SOME_INCREASING_STOCK_TITLE, SOME_DECREASING_STOCK_TITLE);

    List<LocalDate> dates = dto.history.stream().map((portfolio) -> portfolio.date).collect(toList());
    assertThat(dates).containsExactly(SOME_FIRST_DATE, SOME_SECOND_DATE);
  }

  @Test
  public void givenHistoricalPortfolioWithStocks_whenToDto_thenTitleAndQuantityIsReported() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    TreeSet<HistoricalPortfolio> portfolios = new TreeSet<>();
    portfolios.add(someFirstPortfolio);

    PortfolioReportDto dto = assembler.toDto(portfolios, SOME_INCREASING_STOCK_TITLE, SOME_DECREASING_STOCK_TITLE);

    HistoricalPortfolioDto portfolioDto = dto.history.get(0);
    PortfolioItemDto portfolioItemDto = portfolioDto.stocks.get(0);
    assertThat(portfolioItemDto.title).isEqualTo(SOME_TITLE);
    assertThat(portfolioItemDto.quantity).isEqualTo(SOME_QUANTITY);
  }

  @Test
  public void givenHistoricalPortfolioWithStocks_whenToDto_thenTotalValueIsCalculated() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    TreeSet<HistoricalPortfolio> portfolios = new TreeSet<>();
    portfolios.add(someFirstPortfolio);

    PortfolioReportDto dto = assembler.toDto(portfolios, SOME_INCREASING_STOCK_TITLE, SOME_DECREASING_STOCK_TITLE);

    HistoricalPortfolioDto portfolioDto = dto.history.get(0);
    assertThat(portfolioDto.totalValue).isEqualTo(SOME_FIRST_PORTFOLIO_TOTAL.toUsd());
  }

  @Test
  public void givenHistoricalPortfolioWithStocks_whenToDto_thenStockHistoricalValueIsUsed() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    TreeSet<HistoricalPortfolio> portfolios = new TreeSet<>();
    portfolios.add(someFirstPortfolio);

    PortfolioReportDto dto = assembler.toDto(portfolios, SOME_INCREASING_STOCK_TITLE, SOME_DECREASING_STOCK_TITLE);

    HistoricalPortfolioDto portfolioDto = dto.history.get(0);
    PortfolioItemDto portfolioItemDto = portfolioDto.stocks.get(0);
    assertThat(portfolioItemDto.currentValue).isEqualTo(SOME_FIRST_HISTORICAL_MONEY_AMOUNT.toUsd());
  }

  @Test
  public void whenToDto_thenMostVolatileStocksAreMappedCorrectly() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    TreeSet<HistoricalPortfolio> portfolios = new TreeSet<>();
    
    PortfolioReportDto dto = assembler.toDto(portfolios, SOME_INCREASING_STOCK_TITLE, SOME_DECREASING_STOCK_TITLE);

    assertThat(dto.mostIncreasingStock).isEqualTo(SOME_INCREASING_STOCK_TITLE);
    assertThat(dto.mostDecreasingStock).isEqualTo(SOME_DECREASING_STOCK_TITLE);
  }
}
