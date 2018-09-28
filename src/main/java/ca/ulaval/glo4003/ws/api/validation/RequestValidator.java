package ca.ulaval.glo4003.ws.api.validation;

import ca.ulaval.glo4003.ws.infrastructure.injection.Component;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Component
public class RequestValidator {

  public <T> void validate(T requestToValidate) {

    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();
    validatorFactory.close();

    Set<ConstraintViolation<T>> violations = validator.validate(requestToValidate);
    if (!violations.isEmpty()) {
      throw new InvalidInputException(violations);
    }
  }
}
