package ca.ulaval.glo4003.ws.api.authentication;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.service.authentication.AuthenticationService;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TokenRevocationResourceTest {


  @Mock
  private AuthenticationService authenticationService;

  @InjectMocks
  private TokenRevocationResourceImpl revocationResource;

  @Test
  public void whenRevokingToken_thenTokenIsRevoked() {
    revocationResource.revokeToken();

    verify(authenticationService).revokeToken();
  }

  @Test
  public void whenRevokingToken_thenResponseIsOk() {
    Response response = revocationResource.revokeToken();

    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
  }
}