package ca.ulaval.glo4003.infrasctructure.injection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;


public class ServiceLocatorTest {

  private static final SomeComponent SOME_COMPONENT_INSTANCE = new SomeComponent();

  private ServiceLocator serviceLocator;

  @Before
  public void initialize() {
    serviceLocator = new ServiceLocator();
    serviceLocator.registerInstance(SomeComponent.class, SOME_COMPONENT_INSTANCE);
    serviceLocator.discoverPackage(getClass().getPackage().getName(), AnAnnotation.class);
  }

  @Test
  public void givenRegisteredInstance_whenGetting_thenAlwaysReturnsTheSameInstance() {
    SomeComponent gottenComponent = serviceLocator.get(SomeComponent.class);

    assertTrue(SOME_COMPONENT_INSTANCE == gottenComponent);
  }

  @Test
  public void givenRegisteredClass_whenGetting_thenReturnInstantiatedComponent() {
    serviceLocator.register(SomeInjectableComponent.class);

    SomeInjectableComponent gottenComponent = serviceLocator.get(SomeInjectableComponent.class);

    assertThat(gottenComponent).isNotNull();
  }

  @Test(expected = NonInjectableConstructorException.class)
  public void givenAClassWithANonInjectableConstructor_whenInstantiating_thenThrowNonInjectableConstructorException() {
    serviceLocator.register(AClassWithANonInjectableConstructor.class);

    serviceLocator.get(AClassWithANonInjectableConstructor.class);
  }

  @Test(expected = UnregisteredComponentException.class)
  public void givenUnregisteredClass_whenGetting_thenThrowUnregisteredComponentException() {
    serviceLocator.get(SomeInjectableComponent.class);
  }

  @Test
  public void givenAnnotatedComponent_whenDiscoveringPackage_thenComponentIsRegisteredNormally() {
    ADiscoveredComponent gottenComponent = serviceLocator.get(ADiscoveredComponent.class);

    assertThat(gottenComponent).isNotNull();
  }

  @Test
  public void givenDiscoveredPackage_whenGettingClassesForAnnotation_thenAllClassesAreReturned() {
    int expectedNumberOfClasses = 1;

    Set<?> gottenClasses = serviceLocator.getClassesForAnnotation(getClass().getPackage().getName(), AnAnnotation.class);

    assertThat(gottenClasses).hasSize(expectedNumberOfClasses);
  }

  @Test
  public void whenGettingAll_thenReturnAllInheritors() {
    serviceLocator.registerInstance(SomeChildClass.class, new SomeChildClass());
    int expectedNumberOfComponents = 2;

    List<SomeComponent> gottenComponents = serviceLocator.getAll(SomeComponent.class);

    assertThat(gottenComponents).hasSize(expectedNumberOfComponents);
  }

  @Test
  public void givenImplicitlyDeclaredEmptyConstructor_whenInstantiating_thenInjectConstructorAnyway() {
    serviceLocator.register(SomeComponent.class);

    SomeComponent gottenComponent = serviceLocator.get(SomeComponent.class);

    assertThat(gottenComponent).isNotNull();
  }

  @interface AnAnnotation {
  }

  static class SomeComponent {
  }

  public static class SomeInjectableComponent {
    @Inject
    public SomeInjectableComponent(SomeComponent dependency) {
    }
  }

  private static class AClassWithANonInjectableConstructor {
    public AClassWithANonInjectableConstructor(SomeComponent dependency) {
    }
  }


  private static class SomeChildClass extends SomeComponent {
  }
}

@ServiceLocatorTest.AnAnnotation
class ADiscoveredComponent {
  @Inject
  public ADiscoveredComponent(ServiceLocatorTest.SomeComponent dependency) {
  }
}
