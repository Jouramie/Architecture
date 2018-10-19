package ca.ulaval.glo4003.ws.api.stock;

import ca.ulaval.glo4003.service.stock.max.StockMaxValueSinceRange;
import ca.ulaval.glo4003.ws.api.stock.max.StockMaxResponseDto;
import ca.ulaval.glo4003.ws.http.pagination.PaginationBinding;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
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
  @PaginationBinding
  @Operation(
      summary = "Get all stocks.",
      description = "A list of all stocks. Each stock contains a title, market, stock name, "
          + "category, stock value at market opening, current stock value "
          + "and stock value at market close.",
      responses = {
          @ApiResponse(
              description = "Stocks information",
              headers = {
                  @Header(
                      name = "X-Total-Count",
                      description = "The number of available stock.")
              },
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
  @Path("/{title}/max")
  @Operation(summary = "Get stock maximum value.",
      description = "Return the stock maximum value since the interval asked.",
      responses = {@ApiResponse(description = "Stock maximum value",
          content = @Content(schema = @Schema(implementation = StockMaxResponseDto.class))),
          @ApiResponse(responseCode = "400", description = "Missing or invalid since parameter"),
          @ApiResponse(responseCode = "404", description = "Stock does not exist")})
  StockMaxResponseDto getStockMaxValue(
      @PathParam("title")
      @Parameter(
          description = "Title of the stock",
          required = true
      )
          String title,
      @QueryParam("since")
      @Parameter(
          description = "Since parameter",
          schema = @Schema(
              implementation = StockMaxValueSinceRange.class
          ),
          required = true
      )
          String since);
}
