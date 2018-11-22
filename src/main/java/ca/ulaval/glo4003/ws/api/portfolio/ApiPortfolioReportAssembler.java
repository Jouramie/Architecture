package ca.ulaval.glo4003.ws.api.portfolio;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioReportDto;
import ca.ulaval.glo4003.ws.api.portfolio.dto.ApiPortfolioReportResponseDto;

@Component
public class ApiPortfolioReportAssembler {
  public ApiPortfolioReportResponseDto toDto(PortfolioReportDto reportDto) {
    return new ApiPortfolioReportResponseDto(reportDto.history, reportDto.mostIncreasingStock,
        reportDto.mostDecreasingStock);
  }
}
