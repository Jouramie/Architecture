package ca.ulaval.glo4003.service.portfolio;

import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.ws.api.portfolio.PortfolioResponseDto;
import java.util.ArrayList;

public class PortfolioAssembler {
  public PortfolioResponseDto toDto(Portfolio portfolio) {
    return new PortfolioResponseDto(new ArrayList<>(), null);
  }
}
