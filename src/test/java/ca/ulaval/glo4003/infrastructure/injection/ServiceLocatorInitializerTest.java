package ca.ulaval.glo4003.infrastructure.injection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

import javax.inject.Inject;
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

  @Test
  public void givenTwoClassesWithSameDependency_whenConfiguring_thenTheyShareTheSameReferenceToTheDependency() {
    serviceLocatorInitializer.register(Object.class)
        .register(ADependingClass.class)
        .register(ASecondDependingClass.class).configure();

    ADependingClass aDependingClass = serviceLocator.get(ADependingClass.class);
    ASecondDependingClass aSecondDependingClass = serviceLocator.get(ASecondDependingClass.class);

    assertThat(aDependingClass.dependency).isEqualTo(aSecondDependingClass.dependency);
  }
}

class AnInjectableClass {

}

class ADependingClass {

  public final Object dependency;

  @Inject
  public ADependingClass(Object depdendency) {
    dependency = depdendency;
  }
}

class ASecondDependingClass {

  public final Object dependency;

  @Inject
  public ASecondDependingClass(Object dependency) {
    this.dependency = dependency;
  }
}

