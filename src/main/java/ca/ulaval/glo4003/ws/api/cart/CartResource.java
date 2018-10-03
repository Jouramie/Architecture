package ca.ulaval.glo4003.ws.api.cart;

import ca.ulaval.glo4003.ws.http.AuthenticationRequiredBinding;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/cart")
@AuthenticationRequiredBinding
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CartResource {
  @GET
  @Operation(
      summary = "Get the content of the cart.",
      description = "Return the every stocks in the cart, with their details and quantity.",
      responses = {
          @ApiResponse(
              responseCode = "200", description = "cart content",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = CartItemResponseDto.class))
              )
          ),
          @ApiResponse(responseCode = "401", description = "not logged in")
      }
  )
  List<CartItemResponseDto> getCartContent();

  @PUT
  @Path("/{title}")
  @Operation(
      summary = "Add a stock to the cart.",
      description = "Add quantity of a stock to the cart. "
          + "Return every stocks in the cart, with their details and quantity.",
      requestBody = @RequestBody(
          content = @Content(schema = @Schema(implementation = CartStockRequest.class))
      ),
      responses = {
          @ApiResponse(
              responseCode = "200", description = "cart content",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = CartItemResponseDto.class))
              )
          ),
          @ApiResponse(responseCode = "400",
              description = "stock does not exist or quantity is invalid"),
          @ApiResponse(responseCode = "401", description = "not logged in")
      }
  )
  List<CartItemResponseDto> addStockToCart(@Parameter(description = "Title", required = true)
                                         @PathParam("title") String title,
                                         CartStockRequest cartStockRequest);

  @PATCH
  @Path("/{title}")
  @Operation(
      summary = "Update a stock in the cart.",
      description = "Update the quantity of a stock. "
          + "Return every stocks in the cart, with their details and quantity.",
      requestBody = @RequestBody(
          content = @Content(schema = @Schema(implementation = CartStockRequest.class))
      ),
      responses = {
          @ApiResponse(
              responseCode = "200", description = "cart content",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = CartItemResponseDto.class))
              )
          ),
          @ApiResponse(responseCode = "400",
              description = "stock does not exist or quantity is invalid"),
          @ApiResponse(responseCode = "401", description = "not logged in")
      }
  )
  List<CartItemResponseDto> updateStockInCart(@Parameter(description = "Title", required = true)
                                            @PathParam("title") String title,
                                            CartStockRequest cartStockRequest);

  @DELETE
  @Path("/{title}")
  @Operation(
      summary = "Delete a stock in the cart.",
      description = "Remove a stock from the cart. "
          + "Return every stocks in the cart, with their details and quantity.",
      responses = {
          @ApiResponse(
              responseCode = "200", description = "cart content",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = CartItemResponseDto.class))
              )
          ),
          @ApiResponse(responseCode = "401", description = "not logged in")
      }
  )
  List<CartItemResponseDto> deleteStockInCart(@Parameter(description = "Title", required = true)
                                            @PathParam("title") String title,
                                            CartStockRequest cartStockRequest);

  @DELETE
  @Operation(
      summary = "Empty the cart.",
      description = "Remove all stocks from the cart.",
      responses = {
          @ApiResponse(responseCode = "204", description = "cart emptied"),
          @ApiResponse(responseCode = "401", description = "not logged in")
      }
  )
  void emptyCart();

  @POST
  @Path("/checkout")
  @Consumes(MediaType.WILDCARD)
  @Operation(
      summary = "Check out the cart.",
      description = "Check out the current content of the cart. "
          + "Return the checked out stocks, with their details and quantity.",
      responses = {
          @ApiResponse(
              responseCode = "200", description = "cart content",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = CartItemResponseDto.class))
              )
          ),
          @ApiResponse(responseCode = "401", description = "not logged in")
      }
  )
  List<CartItemResponseDto> checkoutCart();
}
