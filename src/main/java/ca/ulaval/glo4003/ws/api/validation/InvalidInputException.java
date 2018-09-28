package ca.ulaval.glo4003.ws.api.validation;

import static java.util.stream.Collectors.toList;

import java.util.Set;
import javax.validation.ConstraintViolation;

public class InvalidInputException extends RuntimeException {

  private static final long serialVersionUID = -5723443749381881618L;
  private final InputErrorResponseModel inputErrors;

  public <T> InvalidInputException(Set<ConstraintViolation<T>> violations) {
    inputErrors =
        new InputErrorResponseModel(violations.stream()
            .map(this::buildErrorMessage)
            .collect(toList()));
  }

  private <T> String buildErrorMessage(ConstraintViolation<T> violation) {
    return violation.getPropertyPath() + " " + violation.getMessage();
  }

  public InputErrorResponseModel getInputErrors() {
    return inputErrors;
  }
}
