package ca.ulaval.glo4003.ws.api.authentication;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/authenticate")
public interface AuthenticationResource {

    @POST
    AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequest);
}
