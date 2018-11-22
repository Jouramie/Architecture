package ca.ulaval.glo4003.ws.api.portfolio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(
    name = "Portfolio report"
)
public class ApiPortfolioReportResponseDto {
  public final List<ApiHistoricalPortfolioResponseDto> history;
  public final String mostIncreasingStock;
  public final String mostDecreasingStock;

  public ApiPortfolioReportResponseDto(List<ApiHistoricalPortfolioResponseDto> history, String mostIncreasingStock,
                                       String mostDecreasingStock) {
    this.history = history;
    this.mostIncreasingStock = mostIncreasingStock;
    this.mostDecreasingStock = mostDecreasingStock;
  }
}
