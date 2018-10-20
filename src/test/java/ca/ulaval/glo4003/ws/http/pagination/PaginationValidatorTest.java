package ca.ulaval.glo4003.ws.http.pagination;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PaginationValidatorTest {
  public static final String FORMAT = "'%s'";
  private static final String PAGE_PARAM = "page";
  private static final String PER_PAGE_PARAM = "per_page";
  private final String SOME_PAGE = "2";
  private final String SOME_PER_PAGE = "5";

  private MultivaluedHashMap<String, String> queryParams;
  @Mock
  private UriInfo uriInfo;
  @Mock
  private ContainerRequestContext requestContext;

  private PaginationValidator paginationValidator;

  @Before
  public void setup() {
    queryParams = new MultivaluedHashMap<>();
    given(uriInfo.getQueryParameters()).willReturn(queryParams);
    given(requestContext.getUriInfo()).willReturn(uriInfo);

    paginationValidator = new PaginationValidator();
  }

  @Test
  public void givenPositivePageAndPerPageQueryParams_whenFiltering_thenRequestNotAbort() {
    queryParams.add(PAGE_PARAM, SOME_PAGE);
    queryParams.add(PER_PAGE_PARAM, SOME_PER_PAGE);

    paginationValidator.filter(requestContext);

    verify(requestContext, never()).abortWith(any(Response.class));
  }

  @Test
  public void givenNegativePerPageQueryParam_whenFiltering_thenBadRequest() {
    queryParams.add(PAGE_PARAM, SOME_PAGE);
    queryParams.add(PER_PAGE_PARAM, "-13");

    paginationValidator.filter(requestContext);

    verifyRequestContextAbortWithBadRequest(format(FORMAT, PER_PAGE_PARAM));
  }

  @Test
  public void givenNegativePageQueryParam_whenFiltering_thenBadRequest() {
    queryParams.add(PAGE_PARAM, "-2");
    queryParams.add(PER_PAGE_PARAM, SOME_PER_PAGE);

    paginationValidator.filter(requestContext);

    verifyRequestContextAbortWithBadRequest(format(FORMAT, PAGE_PARAM));
  }

  @Test
  public void givenTwoInvalidQueryParam_whenFiltering_thenBadRequestWithBothMessages() {
    queryParams.add(PAGE_PARAM, "-2");
    queryParams.add(PER_PAGE_PARAM, "");

    paginationValidator.filter(requestContext);

    verifyRequestContextAbortWithBadRequest(
        format(FORMAT, PAGE_PARAM),
        format(FORMAT, PER_PAGE_PARAM)
    );
  }

  @Test
  public void givenNonNumericalPerPageQueryParam_whenFiltering_thenBadRequest() {
    queryParams.add(PAGE_PARAM, SOME_PAGE);
    queryParams.add(PER_PAGE_PARAM, "NonNumerical");

    paginationValidator.filter(requestContext);

    verifyRequestContextAbortWithBadRequest(format(FORMAT, PER_PAGE_PARAM));
  }

  @Test
  public void givenNonNumericalPageQueryParam_whenFiltering_thenBadRequest() {
    queryParams.add(PAGE_PARAM, "NonNumerical");
    queryParams.add(PER_PAGE_PARAM, SOME_PER_PAGE);

    paginationValidator.filter(requestContext);

    verifyRequestContextAbortWithBadRequest(format(FORMAT, PAGE_PARAM));
  }

  @Test
  public void givenNoPerPageQueryParam_whenFiltering_thenRequestNotAbort() {
    queryParams.add(PAGE_PARAM, SOME_PAGE);

    paginationValidator.filter(requestContext);

    verify(requestContext, never()).abortWith(any(Response.class));
  }

  @Test
  public void givenNoPageQueryParam_whenFiltering_thenRequestNotAbort() {
    queryParams.add(PER_PAGE_PARAM, SOME_PER_PAGE);

    paginationValidator.filter(requestContext);

    verify(requestContext, never()).abortWith(any(Response.class));
  }

  private void verifyRequestContextAbortWithBadRequest(String... messageContent) {
    ArgumentCaptor<Response> argument = ArgumentCaptor.forClass(Response.class);
    verify(requestContext).abortWith(argument.capture());
    assertThat(argument.getValue().getStatus()).isEqualTo(400);
    assertThat(((String) argument.getValue().getEntity())).contains(messageContent);
  }
}
