package ca.ulaval.glo4003.infrastructure.injection;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import ca.ulaval.glo4003.infrastructure.injection.exception.UnregisteredComponentException;
import org.junit.Before;
import org.junit.Test;

public class ServiceLocatorTest {

  public static final Object STORED_INSTANCE = new Object();
  private final ServiceLocator serviceLocator = new ServiceLocator();

  @Before
  public void setUp() {
    serviceLocator.registerInstance(Object.class, STORED_INSTANCE);
  }

  @Test
  public void whenGettingComponent_thenReturnStoredInstance() {
    Object gottenInstance = serviceLocator.get(Object.class);

    assertEquals(STORED_INSTANCE, gottenInstance);
  }

  @Test
  public void whenResetting_thenClearSavedInstances() {
    serviceLocator.reset();

    assertThatThrownBy(() -> serviceLocator.get(Object.class)).isInstanceOf(UnregisteredComponentException.class);
  }
}
