package ca.ulaval.glo4003.portfolio;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import org.junit.Rule;

public class PortfolioReportIT {
  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest(new PortfolioReportITContext());


}
