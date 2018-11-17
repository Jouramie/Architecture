package ca.ulaval.glo4003.ws.http.pagination;

import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.ws.http.authentication.FilterRegistration;
import java.util.List;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

@FilterRegistration
@PaginationBinding
public class PaginationFilter implements ContainerResponseFilter {
  public static final int DEFAULT_PAGE = 1;
  public static final int DEFAULT_PER_PAGE = 15;

  private final Pagination pagination;

  public PaginationFilter() {
    pagination = ServiceLocator.INSTANCE.get(Pagination.class);
  }

  @Override
  public void filter(ContainerRequestContext requestContext,
                     ContainerResponseContext responseContext) {
    if (responseContext.getStatus() != 200) {
      return;
    }
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
    return (page != null) ? Integer.parseInt(page) : DEFAULT_PAGE;
  }

  private int getPerPageQueryParamOrDefault(ContainerRequestContext requestContext) {
    String perPage = requestContext.getUriInfo().getQueryParameters().getFirst("per_page");
    return (perPage != null) ? Integer.parseInt(perPage) : DEFAULT_PER_PAGE;
  }
}
