package ca.ulaval.glo4003.ws.api.portfolio.mappers;

import ca.ulaval.glo4003.service.portfolio.InvalidPortfolioException;
import ca.ulaval.glo4003.ws.api.ErrorMapper;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;


@ErrorMapper
public class InvalidPortfolioExceptionMapper implements ExceptionMapper<InvalidPortfolioException> {

  @Override
  public Response toResponse(InvalidPortfolioException e) {
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
  }
}
