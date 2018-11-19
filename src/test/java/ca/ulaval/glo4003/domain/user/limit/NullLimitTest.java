package ca.ulaval.glo4003.domain.user.limit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class NullLimitTest {

  private NullLimit limit;

  @Test
  public void whenCanProcessTransaction_thenAlwaysYes() {
    limit = new NullLimit(null, null);

    boolean result = limit.canProcessTransaction(null);

    assertThat(result).isTrue();
  }
}
