package ca.ulaval.glo4003.ws.api.stock;

import ca.ulaval.glo4003.service.stock.StockDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/stocks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface StockResource {
  @GET
  @Path("/{title}")
  @Operation(
      summary = "Stock information for given title.",
      description = "Return the stock title, market, stock name, category, "
          + "stock value at market opening, current stock value and stock value at market close.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Stock information",
              content = @Content(
                  schema = @Schema(
                      implementation = StockDto.class
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Stock does not exist"
          )
      }
  )
  StockDto getStockByTitle(
      @PathParam("title")
      @Parameter(
          description = "Title",
          required = true
      )
          String title);

  @GET
  @Operation(
      summary = "Get all stocks.",
      description = "A list of all stocks. Each stock contains a title, market, stock name, "
          + "category, stock value at market opening, current stock value "
          + "and stock value at market close.",
      responses = {
          @ApiResponse(
              description = "Stocks information",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          implementation = StockDto.class
                      )
                  )
              )
          )
      }
  )
  List<StockDto> getStocks(
      @QueryParam("name")
      @Parameter(description = "Stock name")
          String name,
      @QueryParam("category")
      @Parameter(description = "Stock category")
          String category,
      @QueryParam("page")
      @Parameter(
          description = "Page number",
          schema = @Schema(
              defaultValue = "1"
          )
      )
          int page,
      @QueryParam("per_page")
      @Parameter(
          description = "Number of stock per page",
          schema = @Schema(
              defaultValue = "15"
          )
      )
          int perPage);

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
  List<String> getStocksCategories();
}
