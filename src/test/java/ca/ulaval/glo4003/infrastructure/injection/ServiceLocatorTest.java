package ca.ulaval.glo4003.infrastructure.injection;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ServiceLocatorTest {

  private final ServiceLocator serviceLocator = new ServiceLocator();

  @Test
  public void whenGettingComponent_thenReturnStoredInstance() {
    Object storedInstance = new Object();
    serviceLocator.registerInstance(Object.class, storedInstance);

    Object gottenInstance = serviceLocator.get(Object.class);

    assertEquals(storedInstance, gottenInstance);
  }
}
