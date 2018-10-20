package ca.ulaval.glo4003.ws.http.pagination;

import ca.ulaval.glo4003.infrastructure.injection.FilterRegistration;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@FilterRegistration
@PaginationBinding
public class PaginationValidator implements ContainerRequestFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) {
    List<String> errorMessages = new ArrayList<>();
    MultivaluedMap<String, String> queryParameters = requestContext
        .getUriInfo()
        .getQueryParameters();

    if (isInvalidQueryParam(queryParameters, "page")) {
      errorMessages.add("Invalid 'page' query parameter");
    }

    if (isInvalidQueryParam(queryParameters, "per_page")) {
      errorMessages.add("Invalid 'per_page' query parameter");
    }

    if (!errorMessages.isEmpty()) {
      requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST)
          .entity(getFormattedErrorMessage(errorMessages))
          .build());
    }
  }

  private String getFormattedErrorMessage(List<String> errorMessages) {
    String errorMessage = errorMessages.toString();
    return errorMessage.substring(1, errorMessage.length() - 1);
  }

  private boolean isInvalidQueryParam(MultivaluedMap<String, String> queryParameters,
                                      String queryParamName) {
    try {
      String queryParam = queryParameters.getFirst(queryParamName);
      if (queryParam != null && Integer.parseInt(queryParam) < 1) {
        return true;
      }
    } catch (Exception e) {
      return true;
    }
    return false;
  }
}