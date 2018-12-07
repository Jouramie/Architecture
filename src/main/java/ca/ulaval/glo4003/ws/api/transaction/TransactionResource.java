package ca.ulaval.glo4003.ws.api.transaction;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.ws.api.transaction.dto.TransactionModelDto;
import ca.ulaval.glo4003.ws.http.authentication.AuthenticationRequiredBinding;
import java.util.List;
import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Resource
@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource implements DocumentedTransactionResource {
  @GET
  @AuthenticationRequiredBinding(authorizedRoles = {UserRole.ADMINISTRATOR})
  @Override
  public List<TransactionModelDto> getTransactions(@QueryParam("since") String since) {
    return null;
  }
}
