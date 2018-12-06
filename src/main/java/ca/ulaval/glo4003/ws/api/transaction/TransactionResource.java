package ca.ulaval.glo4003.ws.api.transaction;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.ws.api.transaction.dto.TransactionModelDto;
import ca.ulaval.glo4003.ws.http.authentication.AuthenticationRequiredBinding;
import java.util.List;
import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Resource
@Path("/")
public class TransactionResource implements DocumentedTransactionResource {
  @GET
  @Path("/transactions")
  @AuthenticationRequiredBinding(authorizedRoles = {UserRole.ADMINISTRATOR})
  @Override
  public List<TransactionModelDto> getTransactions(@QueryParam("since") String since) {
    return null;
  }

  @GET
  @Path("/users/{email}/transactions")
  @AuthenticationRequiredBinding(authorizedRoles = UserRole.ADMINISTRATOR)
  @Override
  public List<TransactionModelDto> getUserTransactions(@PathParam("email") String email,
                                                       @QueryParam("since") String since) {
    return null;
  }

  @GET
  @Path("/stocks/{title}/transactions")
  @AuthenticationRequiredBinding(authorizedRoles = UserRole.ADMINISTRATOR)
  @Override
  public List<TransactionModelDto> getStockTransactions(@PathParam("title") String title,
                                                        @QueryParam("since") String since) {
    return null;
  }
}
