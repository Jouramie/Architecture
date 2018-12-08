package ca.ulaval.glo4003.ws.api.transaction;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.service.date.SinceParameter;
import ca.ulaval.glo4003.service.transaction.TransactionService;
import ca.ulaval.glo4003.ws.api.transaction.assemblers.ApiTransactionAssembler;
import ca.ulaval.glo4003.ws.api.transaction.dto.ApiTransactionDto;
import ca.ulaval.glo4003.ws.api.util.SinceParameterConverter;
import ca.ulaval.glo4003.ws.http.authentication.AuthenticationRequiredBinding;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/stocks/{title}/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Resource
public class StockTransactionResource implements DocumentedStockTransactionResource {
  private final TransactionService transactionService;
  private final ApiTransactionAssembler transactionAssembler;
  private final SinceParameterConverter sinceParameterConverter;

  @Inject
  public StockTransactionResource(TransactionService transactionService, ApiTransactionAssembler transactionAssembler, SinceParameterConverter sinceParameterConverter) {
    this.transactionService = transactionService;
    this.transactionAssembler = transactionAssembler;
    this.sinceParameterConverter = sinceParameterConverter;
  }

  @GET
  @AuthenticationRequiredBinding(authorizedRoles = UserRole.ADMINISTRATOR)
  @Override
  public List<ApiTransactionDto> getStockTransactions(@PathParam("title") String title,
                                                      @QueryParam("since") String since) {
    SinceParameter sinceParameter = sinceParameterConverter.convertSinceParameter(since);
    return transactionAssembler.toDtoList(transactionService.getTransactionsByTitle(title, sinceParameter));
  }
}
