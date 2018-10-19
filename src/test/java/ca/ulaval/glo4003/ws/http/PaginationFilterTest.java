package ca.ulaval.glo4003.ws.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.ws.pagination.Pagination;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.UriInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PaginationFilterTest {

  private final List<Object> responseBody = new ArrayList<>();
  private final List<Object> paginatedResponse = new ArrayList<>();
  private final int page = 2;
  private final int perPage = 5;
  private MultivaluedHashMap<String, String> headers;
  private MultivaluedHashMap<String, String> queryParams;
  @Mock
  private UriInfo uriInfo;
  @Mock
  private ContainerRequestContext requestContext;
  @Mock
  private ContainerResponseContext responseContext;
  @Mock
  private Pagination pagination;
  private PaginationFilter paginationFilter;

  @Before
  public void setup() {
    queryParams = new MultivaluedHashMap<>();
    queryParams.add("page", Integer.toString(page));
    queryParams.add("per_page", Integer.toString(perPage));
    given(uriInfo.getQueryParameters()).willReturn(queryParams);
    given(requestContext.getUriInfo()).willReturn(uriInfo);

    headers = new MultivaluedHashMap<>();
    given(requestContext.getHeaders()).willReturn(headers);

    given(responseContext.getEntity()).willReturn(responseBody);
    given(pagination.getPaginatedResponse(responseBody, page, perPage))
        .willReturn(paginatedResponse);

    ServiceLocator.INSTANCE.registerInstance(Pagination.class, pagination);
    paginationFilter = new PaginationFilter();
  }

  @Test
  public void whenFiltering_thenResponseIsPaginated() {
    paginationFilter.filter(requestContext, responseContext);

    verify(pagination).getPaginatedResponse(responseBody, page, perPage);
    verify(responseContext).setEntity(paginatedResponse);
  }

  @Test
  public void whenFiltering_thenXTotalCountAddedToHeader() {
    paginationFilter.filter(requestContext, responseContext);

    assertThat(headers.get("X-Total-Count")).contains(Integer.toString(responseBody.size()));
  }

  @Test
  public void givenNoPageQueryParam_whenFiltering_thenUseDefaultPage() {
    queryParams.remove("page");

    paginationFilter.filter(requestContext, responseContext);

    verify(pagination).getPaginatedResponse(responseBody, 1, perPage);
  }

  @Test
  public void givenNoPerPageQueryParam_whenFiltering_thenUseDefaultPage() {
    queryParams.remove("per_page");

    paginationFilter.filter(requestContext, responseContext);

    verify(pagination).getPaginatedResponse(responseBody, page, 15);
  }
}
