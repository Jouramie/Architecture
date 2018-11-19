package ca.ulaval.glo4003.domain.user.limit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class NullLimitTest {

  private NullLimit limit;

  @Test
  public void whenVerified_thenTransactionNeverExceedLimit() {
    limit = new NullLimit();

    boolean result = limit.doesTransactionExceedLimit(null);

    assertThat(result).isFalse();
  }
}
