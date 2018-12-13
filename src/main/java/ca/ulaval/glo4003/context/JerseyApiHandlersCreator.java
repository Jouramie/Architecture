package ca.ulaval.glo4003.context;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.ws.api.ErrorMapper;
import ca.ulaval.glo4003.ws.http.FilterRegistration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Resource;
import javax.ws.rs.core.Application;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;
import org.reflections.Reflections;

public class JerseyApiHandlersCreator {

  public List<Handler> createHandlers(ServiceLocator serviceLocator, String webServicePackagePrefix) {
    return new ArrayList<>(Arrays.asList(createApiHandler(serviceLocator, webServicePackagePrefix)));
  }

  private Handler createApiHandler(ServiceLocator serviceLocator, String webServicePackagePrefix) {
    Application application = new Application() {
      @Override
      public Set<Object> getSingletons() {
        return createRegisteredComponentInstances(serviceLocator, webServicePackagePrefix);
      }
    };

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/api/");
    ResourceConfig resourceConfig = ResourceConfig.forApplication(application);
    resourceConfig.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
    setupJacksonSerialization(resourceConfig);
    getClassesForAnnotation(
        webServicePackagePrefix,
        FilterRegistration.class)
        .forEach(resourceConfig::register);

    ServletContainer servletContainer = new ServletContainer(resourceConfig);
    ServletHolder servletHolder = new ServletHolder(servletContainer);
    context.addServlet(servletHolder, "/*");

    return context;
  }

  private Set<Object> createRegisteredComponentInstances(ServiceLocator serviceLocator, String webServicePackagePrefix) {
    List<Class<?>> registeredClasses = Stream.of(Resource.class, ErrorMapper.class, Component.class)
        .map(annotation -> getClassesForAnnotation(webServicePackagePrefix, annotation))
        .flatMap(Collection::stream).collect(toList());
    registeredClasses.add(OpenApiResource.class);

    return registeredClasses.stream()
        .map(serviceLocator::get)
        .collect(toSet());
  }

  private void setupJacksonSerialization(ResourceConfig resourceConfig) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
    provider.setMapper(mapper);
    resourceConfig.register(provider);
  }

  private Set<Class<?>> getClassesForAnnotation(String packagePrefix,
                                                Class<? extends Annotation> annotation) {
    return new HashSet<>(new Reflections(packagePrefix).getTypesAnnotatedWith(annotation));
  }
}
