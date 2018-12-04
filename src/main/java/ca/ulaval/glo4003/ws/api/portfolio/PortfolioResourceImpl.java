package ca.ulaval.glo4003.ws.api.portfolio;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.service.InternalErrorException;
import ca.ulaval.glo4003.service.date.DateService;
import ca.ulaval.glo4003.service.portfolio.PortfolioService;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioReportDto;
import ca.ulaval.glo4003.ws.api.portfolio.assemblers.ApiPortfolioAssembler;
import ca.ulaval.glo4003.ws.api.portfolio.assemblers.ApiPortfolioReportAssembler;
import ca.ulaval.glo4003.ws.api.portfolio.dto.ApiPortfolioReportResponseDto;
import ca.ulaval.glo4003.ws.api.portfolio.dto.ApiPortfolioResponseDto;
import ca.ulaval.glo4003.ws.api.portfolio.dto.SinceParameter;
import ca.ulaval.glo4003.ws.http.authentication.AuthenticationRequiredBinding;
import java.time.LocalDate;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/portfolio")
@AuthenticationRequiredBinding(acceptedRoles = UserRole.INVESTOR)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Resource
public class PortfolioResourceImpl implements DocumentedPortfolioResource {
  private final PortfolioService portfolioService;
  private final ApiPortfolioAssembler apiPortfolioAssembler;
  private final ApiPortfolioReportAssembler apiPortfolioReportAssembler;
  private final DateService dateService;

  @Inject
  public PortfolioResourceImpl(PortfolioService portfolioService, ApiPortfolioAssembler apiPortfolioAssembler,
                               ApiPortfolioReportAssembler apiPortfolioReportAssembler, DateService dateService) {
    this.portfolioService = portfolioService;
    this.apiPortfolioAssembler = apiPortfolioAssembler;
    this.apiPortfolioReportAssembler = apiPortfolioReportAssembler;
    this.dateService = dateService;
  }

  @GET
  @Override
  public ApiPortfolioResponseDto getPortfolio() {
    PortfolioDto responseDto = portfolioService.getPortfolio();
    return apiPortfolioAssembler.toDto(responseDto);
  }

  @GET
  @Path("/report")
  @Override
  public ApiPortfolioReportResponseDto getPortfolioReport(@QueryParam("since") String since) {
    if (since == null) {
      throw new BadRequestException("Missing 'since' parameter");
    }

    SinceParameter sinceValue;
    try {
      sinceValue = SinceParameter.valueOf(since);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid 'since' parameter");
    }

    LocalDate from = getFromDate(sinceValue);
    PortfolioReportDto reportDto = portfolioService.getPortfolioReport(from);
    return apiPortfolioReportAssembler.toDto(reportDto);
  }

  private LocalDate getFromDate(SinceParameter sinceParameter) {
    switch (sinceParameter) {
      case LAST_FIVE_DAYS:
        return dateService.getFiveDaysAgo();
      case LAST_THIRTY_DAYS:
        return dateService.getThirtyDaysAgo();
      case LAST_YEAR:
        return dateService.getOneYearAgo();
      default:
        throw new InternalErrorException("Missing enum case in DocumentedPortfolioResource.");
    }
  }
}
