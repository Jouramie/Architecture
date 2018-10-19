package ca.ulaval.glo4003.ws.http;

import ca.ulaval.glo4003.infrastructure.injection.FilterRegistration;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.ws.pagination.Pagination;
import java.util.List;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

@FilterRegistration
@PaginationBinding
public class PaginationFilter implements ContainerResponseFilter {
  private final Pagination pagination;

  public PaginationFilter() {
    pagination = ServiceLocator.INSTANCE.get(Pagination.class);
  }

  @Override
  public void filter(ContainerRequestContext requestContext,
                     ContainerResponseContext responseContext) {
    List initialResponse = (List) responseContext.getEntity();
    responseContext.getHeaders().add("X-Total-Count",
        Integer.toString(initialResponse.size()));
    List paginatedResponse = pagination.getPaginatedResponse(
        initialResponse,
        getPageQueryParamOrDefault(requestContext),
        getPerPageQueryParamOrDefault(requestContext)
    );
    responseContext.setEntity(paginatedResponse);
  }

  private int getPageQueryParamOrDefault(ContainerRequestContext requestContext) {
    String page = requestContext.getUriInfo().getQueryParameters().getFirst("page");
    return (page != null) ? Integer.parseInt(page) : 1;
  }

  private int getPerPageQueryParamOrDefault(ContainerRequestContext requestContext) {
    String perPage = requestContext.getUriInfo().getQueryParameters().getFirst("per_page");
    return (perPage != null) ? Integer.parseInt(perPage) : 15;
  }
}
