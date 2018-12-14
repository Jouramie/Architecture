package ca.ulaval.glo4003.service.portfolio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.portfolio.InvalidStockInPortfolioException;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.exception.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioItemDto;
import ca.ulaval.glo4003.util.TestStockBuilder;
import java.math.BigDecimal;
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
  private static final MoneyAmount SOME_PORTFOLIO_TOTAL = new MoneyAmount(SOME_CURRENT_MONEY_AMOUNT.toUsd()
      .multiply(new BigDecimal(SOME_QUANTITY)), Currency.USD);

  private StockCollection someStockCollection;
  private Stock someStock;

  @Mock
  private StockRepository someStockRepository;
  @Mock
  private Portfolio somePortfolio;

  private PortfolioAssembler portfolioAssembler;

  @Before
  public void setupPortfolioAssembler() throws StockNotFoundException, InvalidStockInPortfolioException {
    portfolioAssembler = new PortfolioAssembler(someStockRepository);

    setupStockRepository();
    setupTestPortfolio();
  }

  private void setupStockRepository() {
    given(someStockRepository.exists(SOME_TITLE)).willReturn(true);
    someStock = new TestStockBuilder()
        .withTitle(SOME_TITLE)
        .withCloseValue(SOME_CURRENT_MONEY_AMOUNT)
        .build();
  }

  private void setupTestPortfolio() throws StockNotFoundException, InvalidStockInPortfolioException {
    someStockCollection = new StockCollection().add(SOME_TITLE, SOME_QUANTITY, someStockRepository);
    given(somePortfolio.getStocks()).willReturn(someStockCollection);
    given(somePortfolio.getQuantity(SOME_TITLE)).willReturn(SOME_QUANTITY);
    given(someStockRepository.findByTitle(SOME_TITLE)).willReturn(someStock);
    given(somePortfolio.getCurrentTotalValue(someStockRepository)).willReturn(SOME_PORTFOLIO_TOTAL);
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
