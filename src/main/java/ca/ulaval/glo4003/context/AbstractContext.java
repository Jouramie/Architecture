package ca.ulaval.glo4003.context;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocatorInitializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.glassfish.jersey.server.ResourceConfig;
import org.reflections.Reflections;

public abstract class AbstractContext {

  protected final ServiceLocator serviceLocator;

  public AbstractContext() {
    serviceLocator = ServiceLocator.INSTANCE;
  }

  protected void setupJacksonJavaTimeModule(ResourceConfig resourceConfig) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
    provider.setMapper(mapper);
    resourceConfig.register(provider);
  }

  public abstract void configureDestruction();

  public ContextHandlerCollection createJettyContextHandlers() {
    ContextHandlerCollection contexts = new ContextHandlerCollection();
    contexts.setHandlers(new Handler[] {
        createApiHandler(),
        createSwaggerUiHandler()
    });

    return contexts;
  }

  protected abstract Handler createApiHandler();

  protected abstract Handler createSwaggerUiHandler();

  public void configureApplication(String apiUrl) {
    createServiceLocatorInitializer().configure();
    loadData();
    createSwaggerApi(apiUrl);
  }

  protected abstract ServiceLocatorInitializer createServiceLocatorInitializer();

  protected abstract void loadData();

  protected abstract void createSwaggerApi(String apiUrl);

  protected Set<Class<?>> getClassesForAnnotation(String packagePrefix,
                                                Class<? extends Annotation> annotation) {
    return new HashSet<>(new Reflections(packagePrefix).getTypesAnnotatedWith(annotation));
  }
}
