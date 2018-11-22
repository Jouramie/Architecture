package ca.ulaval.glo4003.ws.api.portfolio.dto;

import ca.ulaval.glo4003.service.portfolio.dto.HistoricalPortfolioDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(
    name = "Portfolio report"
)
public class ApiPortfolioReportResponseDto {
  public final List<HistoricalPortfolioDto> history;
  public final String mostIncreasingStock;
  public final String mostDecreasingStock;

  public ApiPortfolioReportResponseDto(List<HistoricalPortfolioDto> history, String mostIncreasingStock,
                                       String mostDecreasingStock) {
    this.history = history;
    this.mostIncreasingStock = mostIncreasingStock;
    this.mostDecreasingStock = mostDecreasingStock;
  }
}
