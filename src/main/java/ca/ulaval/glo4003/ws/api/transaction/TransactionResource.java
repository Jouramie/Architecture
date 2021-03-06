package ca.ulaval.glo4003.ws.api.transaction;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.service.date.SinceParameter;
import ca.ulaval.glo4003.service.transaction.TransactionDto;
import ca.ulaval.glo4003.service.transaction.TransactionService;
import ca.ulaval.glo4003.ws.api.transaction.assembler.ApiTransactionAssembler;
import ca.ulaval.glo4003.ws.api.transaction.dto.ApiTransactionDto;
import ca.ulaval.glo4003.ws.api.util.SinceParameterConverter;
import ca.ulaval.glo4003.ws.http.authentication.AuthenticationRequiredBinding;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Resource
public class TransactionResource implements DocumentedTransactionResource {
  private final TransactionService transactionService;
  private final ApiTransactionAssembler transactionAssembler;
  private final SinceParameterConverter sinceParameterConverter;

  @Inject
  public TransactionResource(TransactionService transactionService, ApiTransactionAssembler transactionAssembler, SinceParameterConverter sinceParameterConverter) {
    this.transactionService = transactionService;
    this.transactionAssembler = transactionAssembler;
    this.sinceParameterConverter = sinceParameterConverter;
  }

  @GET
  @AuthenticationRequiredBinding(authorizedRoles = {UserRole.ADMINISTRATOR})
  @Override
  public List<ApiTransactionDto> getTransactions(@QueryParam("since") String since) {
    SinceParameter sinceParameter = sinceParameterConverter.convertSinceParameter(since);
    List<TransactionDto> allTransactions = transactionService.getAllTransactions(sinceParameter);
    return transactionAssembler.toDtoList(allTransactions);
  }
}
