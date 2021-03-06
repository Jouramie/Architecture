package ca.ulaval.glo4003.ws.api.portfolio;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.service.date.SinceParameter;
import ca.ulaval.glo4003.service.portfolio.PortfolioService;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioReportDto;
import ca.ulaval.glo4003.ws.api.portfolio.assembler.ApiPortfolioAssembler;
import ca.ulaval.glo4003.ws.api.portfolio.assembler.ApiPortfolioReportAssembler;
import ca.ulaval.glo4003.ws.api.portfolio.dto.ApiPortfolioReportResponseDto;
import ca.ulaval.glo4003.ws.api.portfolio.dto.ApiPortfolioResponseDto;
import ca.ulaval.glo4003.ws.api.util.SinceParameterConverter;
import ca.ulaval.glo4003.ws.http.authentication.AuthenticationRequiredBinding;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/portfolio")
@AuthenticationRequiredBinding(authorizedRoles = UserRole.INVESTOR)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Resource
public class PortfolioResource implements DocumentedPortfolioResource {
  private final PortfolioService portfolioService;
  private final ApiPortfolioAssembler apiPortfolioAssembler;
  private final ApiPortfolioReportAssembler apiPortfolioReportAssembler;
  private final SinceParameterConverter sinceParameterConverter;

  @Inject
  public PortfolioResource(PortfolioService portfolioService,
                           ApiPortfolioAssembler apiPortfolioAssembler,
                           ApiPortfolioReportAssembler apiPortfolioReportAssembler,
                           SinceParameterConverter sinceParameterConverter) {
    this.portfolioService = portfolioService;
    this.apiPortfolioAssembler = apiPortfolioAssembler;
    this.apiPortfolioReportAssembler = apiPortfolioReportAssembler;
    this.sinceParameterConverter = sinceParameterConverter;
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
    SinceParameter sinceValue = sinceParameterConverter.convertSinceParameter(since);
    PortfolioReportDto reportDto = portfolioService.getPortfolioReport(sinceValue);
    return apiPortfolioReportAssembler.toDto(reportDto);
  }
}
