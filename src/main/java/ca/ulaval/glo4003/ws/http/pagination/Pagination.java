package ca.ulaval.glo4003.ws.http.pagination;

import ca.ulaval.glo4003.infrastructure.injection.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class Pagination {

  public <T> List<T> getPaginatedResponse(List<T> response, int page, int perPage) {
    checkIfRequestedPageIsValid(page, perPage);

    int pageFirstIndex = getPageFirstIndex(page, perPage);
    int pageLastIndex = getPageLastIndex(page, perPage, response.size());

    if (pageFirstIndex > response.size()) {
      return new ArrayList<>();
    }
    return response.subList(pageFirstIndex, pageLastIndex);
  }

  private void checkIfRequestedPageIsValid(int page, int perPage) {
    if (page < 0) {
      throw new RuntimeException();
    }
    if (perPage < 0) {
      throw new RuntimeException();
    }
  }

  private int getPageFirstIndex(int page, int perPage) {
    return (page - 1) * perPage;
  }

  private int getPageLastIndex(int page, int perPage, int responseSize) {
    return (page * perPage < responseSize)
        ? page * perPage
        : responseSize;
  }
}
