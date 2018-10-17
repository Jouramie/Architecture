package ca.ulaval.glo4003.ws.api.stock;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/stock_categories")
@Produces(MediaType.APPLICATION_JSON)
public interface StockCategoryResource {


  @GET
  @Operation(
      summary = "Categories.",
      description = "Return the available categories.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Category.",
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
