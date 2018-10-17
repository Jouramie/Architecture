package ca.ulaval.glo4003.ws;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.ws.pagination.Pagination;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class PaginationTest {

  private static final int FIRST_PAGE = 1;
  private static final int SOME_PAGE = 2;
  private static final int LAST_PAGE = 4;
  private static final int OVER_LAST_PAGE = 14;

  private static final int PER_PAGE = 3;
  private static final int GREATER_THEN_TOTAL_PER_PAGE = 14;

  private List<Integer> response;

  private Pagination pagination;

  @Before
  public void setup() {
    response = generateResponse(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    pagination = new Pagination();
  }

  @Test
  public void whenPaginateResponse_thenPageSizeIsRight() {
    List<Integer> paginateResponse = pagination.getPaginatedResponse(response, SOME_PAGE, PER_PAGE);

    assertThat(paginateResponse.size()).isEqualTo(PER_PAGE);
  }

  @Test
  public void whenPaginateResponse_thenPageContainsItemsIsTheSameOrder() {
    List<Integer> paginateResponse = pagination.getPaginatedResponse(response, SOME_PAGE, PER_PAGE);

    assertThat(paginateResponse).containsExactly(4, 5, 6);
  }

  @Test
  public void givenLastPage_whenPaginateResponse_thenPageSizeIsLess() {
    List<Integer> paginateResponse = pagination.getPaginatedResponse(response, LAST_PAGE, PER_PAGE);

    assertThat(paginateResponse.size()).isEqualTo(1);
  }

  @Test
  public void givenLastPage_whenPaginateResponse_thenPageContainsLastItem() {
    List<Integer> paginateResponse = pagination.getPaginatedResponse(response, LAST_PAGE, PER_PAGE);

    assertThat(paginateResponse).contains(10);
  }

  @Test
  public void givenOverLastPage_whenPaginateResponse_thenPageIsEmpty() {
    List<Integer> paginateResponse = pagination.getPaginatedResponse(response, OVER_LAST_PAGE, PER_PAGE);

    assertThat(paginateResponse).isEmpty();
  }

  @Test
  public void givenPerPageGreaterThenTotal_whenPaginateResponse_thenPageContainsAllItems() {
    List<Integer> paginateResponse = pagination.getPaginatedResponse(response, FIRST_PAGE, GREATER_THEN_TOTAL_PER_PAGE);

    assertThat(paginateResponse).isEqualTo(response);
  }

  @SafeVarargs
  private final <T> List<T> generateResponse(T... responseValues) {
    return new ArrayList<>(Arrays.asList(responseValues));
  }
}
