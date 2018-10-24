package ca.ulaval.glo4003.ws.api.validation;

import static java.util.stream.Collectors.toList;

import java.util.Set;
import javax.validation.ConstraintViolation;

public class InvalidInputException extends RuntimeException {

  private final InputErrorResponse inputErrors;

  <T> InvalidInputException(Set<ConstraintViolation<T>> violations) {
    inputErrors =
        new InputErrorResponse(violations.stream()
            .map(this::buildErrorMessage)
            .collect(toList()));
  }

  private <T> String buildErrorMessage(ConstraintViolation<T> violation) {
    return violation.getPropertyPath() + " " + violation.getMessage();
  }

  public InputErrorResponse getInputErrors() {
    return inputErrors;
  }
}
