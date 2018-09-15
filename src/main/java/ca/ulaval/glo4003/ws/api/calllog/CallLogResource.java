package ca.ulaval.glo4003.ws.api.calllog;


import ca.ulaval.glo4003.ws.api.calllog.dto.CallLogDto;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/telephony/calllogs")
public interface CallLogResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<CallLogDto> getCallLogs();

  @DELETE
  @Path("{id}")
  void deleteCallLog(@PathParam("id") String id);
}
