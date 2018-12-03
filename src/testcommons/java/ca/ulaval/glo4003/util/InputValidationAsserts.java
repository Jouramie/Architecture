package ca.ulaval.glo4003.util;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.ws.api.validation.InvalidInputException;
import java.util.regex.Pattern;


public class InputValidationAsserts {

  private static final String ERROR_MESSAGE_PATTERN = "%s.+";

  public static void assertThatExceptionContainsErrorFor(InvalidInputException exception, String field) {
    String expectMessageErrorPattern = String.format(ERROR_MESSAGE_PATTERN, field);
    assertThat(exception.getInputErrors().inputErrors)
        .anyMatch(errorMessage -> Pattern.matches(expectMessageErrorPattern, errorMessage));
  }
}
