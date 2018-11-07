package ca.ulaval.glo4003.ws.api.user;

import ca.ulaval.glo4003.ws.http.AuthenticationRequiredBinding;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/users/{email}/limit")
@AuthenticationRequiredBinding
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UserLimitResource {
  @PUT
  public void setUserLimit(
      @PathParam("email") String email,
      @Valid ApiUserLimitDto apiUserLimitDto);

  @DELETE
  public void removeUserLimit(@PathParam("email") String email);
}
