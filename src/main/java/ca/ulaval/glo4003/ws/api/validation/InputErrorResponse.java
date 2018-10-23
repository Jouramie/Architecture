package ca.ulaval.glo4003.ws.api.validation;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(
    name = "Input errors"
)
public class InputErrorResponse {

  public final List<String> inputErrors;

  InputErrorResponse(List<String> inputErrors) {
    this.inputErrors = inputErrors;
  }
}
