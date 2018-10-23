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
      summary = "Get a stock for a given title.",
      description = "Return the details of the stock with the corresponding title.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  schema = @Schema(
                      implementation = StockDto.class
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Stock does not exist."
          )
      }
  )
  StockDto getStockByTitle(@PathParam("title") String title);

  @GET
  @PaginationBinding
  @Operation(
      summary = "Get all stocks.",
      description = "Return a list of all stocks with their information. ",
      responses = {
          @ApiResponse(
              responseCode = "200",
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
      @Parameter(description = "Search stock by name.")
          String name,
      @QueryParam("category")
      @Parameter(description = "Search stock by category.")
          String category,
      @QueryParam("page")
      @Parameter(
          description = "The page to display",
          schema = @Schema(
              defaultValue = "1"
          )
      )
          int page,
      @QueryParam("per_page")
      @Parameter(
          description = "The number of stock per page",
          schema = @Schema(
              defaultValue = "15"
          )
      )
          int perPage);

  @GET
  @Path("/{title}/max")
  @Operation(summary = "Get the maximum value for a given stock title.",
      description = "Return the stock maximum value since the interval asked.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  schema = @Schema(
                      implementation = StockMaxResponseDto.class
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "'since' parameter is missing or invalid."
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Stock does not exist."
          )
      }
  )
  StockMaxResponseDto getStockMaxValue(
      @PathParam("title")
          String title,
      @QueryParam("since")
      @Parameter(
          description = "Specify a range where the maximum value will be searched for.",
          schema = @Schema(
              implementation = StockMaxValueSinceRange.class
          ),
          required = true
      )
          String since);
}
