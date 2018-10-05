package ca.ulaval.glo4003.service.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationTokenDto;
import org.junit.Test;

public class AuthenticationTokenAssemblerTest {

  private static final String SOME_EMAIL = "email";

  private static final String SOME_TOKEN = "token";

  private final AuthenticationTokenAssembler authenticationTokenAssembler
      = new AuthenticationTokenAssembler();

  @Test
  public void whenAssemblingToModel_thenAssembleFieldByField() {
    AuthenticationTokenDto tokenDto = new AuthenticationTokenDto(SOME_EMAIL, SOME_TOKEN);

    AuthenticationToken token = authenticationTokenAssembler.toModel(tokenDto);

    AuthenticationToken expectedToken = new AuthenticationToken(SOME_TOKEN, SOME_EMAIL);
    assertThat(token).isEqualToComparingFieldByField(expectedToken);
  }
}
