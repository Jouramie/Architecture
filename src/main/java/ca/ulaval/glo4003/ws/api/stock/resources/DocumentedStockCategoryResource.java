package ca.ulaval.glo4003.ws.api.stock.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;

public interface DocumentedStockCategoryResource {

  @Operation(
      summary = "Get stock categories.",
      description = "Return the available categories.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          implementation = String.class
                      )
                  )
              )
          )
      }
  )
  List<String> getCategories();
}
