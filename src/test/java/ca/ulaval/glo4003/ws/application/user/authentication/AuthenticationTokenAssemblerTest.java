package ca.ulaval.glo4003.ws.application.user.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.ws.api.authentication.AuthenticationTokenDto;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationToken;
import org.junit.Test;

public class AuthenticationTokenAssemblerTest {

  private static final String USERNAME = "username";

  private static final String TOKEN = "token";

  private AuthenticationTokenAssembler authenticationTokenAssembler
      = new AuthenticationTokenAssembler();

  @Test
  public void whenAssemblingToModel_thenAssembleFieldByField() {
    AuthenticationTokenDto tokenDto = new AuthenticationTokenDto(USERNAME, TOKEN);

    AuthenticationToken token = authenticationTokenAssembler.toModel(tokenDto);

    AuthenticationToken expectedToken = new AuthenticationToken(TOKEN, USERNAME);
    assertThat(token).isEqualToComparingFieldByField(expectedToken);
  }
}