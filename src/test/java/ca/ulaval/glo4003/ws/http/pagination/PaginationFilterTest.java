package ca.ulaval.glo4003.ws.http.pagination;

import static ca.ulaval.glo4003.ws.http.pagination.PaginationFilter.DEFAULT_PAGE;
import static ca.ulaval.glo4003.ws.http.pagination.PaginationFilter.DEFAULT_PER_PAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
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
  private final int SOME_PAGE = 2;
  private final int SOME_PER_PAGE = 5;

  private final List<Object> responseBody = new ArrayList<>();
  private final List<Object> paginatedResponse = new ArrayList<>();
  private MultivaluedHashMap<String, Object> headers;
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
    queryParams.add("page", Integer.toString(SOME_PAGE));
    queryParams.add("per_page", Integer.toString(SOME_PER_PAGE));
    given(uriInfo.getQueryParameters()).willReturn(queryParams);
    given(requestContext.getUriInfo()).willReturn(uriInfo);

    headers = new MultivaluedHashMap<>();
    given(responseContext.getHeaders()).willReturn(headers);

    given(responseContext.getStatus()).willReturn(200);
    given(responseContext.getEntity()).willReturn(responseBody);
    given(pagination.getPaginatedResponse(responseBody, SOME_PAGE, SOME_PER_PAGE))
        .willReturn(paginatedResponse);

    ServiceLocator.INSTANCE.registerInstance(Pagination.class, pagination);
    paginationFilter = new PaginationFilter();
  }

  @Test
  public void whenFiltering_thenResponseIsPaginated() {
    paginationFilter.filter(requestContext, responseContext);

    verify(pagination).getPaginatedResponse(responseBody, SOME_PAGE, SOME_PER_PAGE);
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

    verify(pagination).getPaginatedResponse(responseBody, DEFAULT_PAGE, SOME_PER_PAGE);
  }

  @Test
  public void givenNoPerPageQueryParam_whenFiltering_thenUseDefaultPage() {
    queryParams.remove("per_page");

    paginationFilter.filter(requestContext, responseContext);

    verify(pagination).getPaginatedResponse(responseBody, SOME_PAGE, DEFAULT_PER_PAGE);
  }

  @Test
  public void givenUnsuccessfulResponse_whenFiltering_thenDoNothing() {
    given(responseContext.getStatus()).willReturn(400);
    given(responseContext.getEntity()).willReturn("Error message");

    paginationFilter.filter(requestContext, responseContext);

    verify(pagination, never()).getPaginatedResponse(anyList(), anyInt(), anyInt());
  }
}
