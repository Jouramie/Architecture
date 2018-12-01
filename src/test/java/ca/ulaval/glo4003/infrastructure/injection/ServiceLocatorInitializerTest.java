package ca.ulaval.glo4003.infrastructure.injection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class ServiceLocatorInitializerTest {

  private ServiceLocator serviceLocator;
  private ServiceLocatorInitializer serviceLocatorInitializer;

  @Before
  public void setUp() {
    serviceLocator = new ServiceLocator();
    serviceLocatorInitializer = new ServiceLocatorInitializer(serviceLocator);
  }

  @Test
  public void whenConfiguring_thenInstantiateRegisteredClasses() {
    serviceLocatorInitializer.register(AnInjectableClass.class).configure();

    AnInjectableClass savedInstance = serviceLocator.get(AnInjectableClass.class);
    assertThat(savedInstance).isInstanceOf(AnInjectableClass.class);
  }

  @Test
  public void givenRegisteredInstance_whenConfiguring_thenThatInstanceIsSavedAsIs() {
    Object anObject = mock(Object.class);

    serviceLocatorInitializer.registerInstance(Object.class, anObject).configure();

    assertThat(serviceLocator.get(Object.class)).isEqualTo(anObject);
  }
}

class AnInjectableClass {

}
