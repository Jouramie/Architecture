package ca.ulaval.glo4003.ws.api.cart;

import ca.ulaval.glo4003.ws.api.cart.dto.ApiCartItemResponseDto;
import ca.ulaval.glo4003.ws.api.cart.dto.ApiTransactionDto;
import ca.ulaval.glo4003.ws.api.cart.dto.CartStockRequestDto;
import ca.ulaval.glo4003.ws.http.AuthenticationRequiredBinding;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
      description = "Return every stock in the cart, with their details and quantity.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          implementation = ApiCartItemResponseDto.class
                      )
                  )
              )
          ),
          @ApiResponse(
              responseCode = "401",
              description = "User is not logged in."
          )
      }
  )
  List<ApiCartItemResponseDto> getCartContent();

  @PUT
  @Path("/{title}")
  @Operation(
      summary = "Add a stock to the cart.",
      description = "Add quantity of a stock to the cart. "
          + "Return every stocks in the cart, with their details and quantity.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Stock successfully added to cart.",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          implementation = ApiCartItemResponseDto.class
                      )
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Stock does not exist or quantity is invalid."
          ),
          @ApiResponse(
              responseCode = "401",
              description = "User is not logged in."
          )
      }
  )
  List<ApiCartItemResponseDto> addStockToCart(
      @PathParam("title") String title,
      CartStockRequestDto cartStockRequestDto);

  @PATCH
  @Path("/{title}")
  @Operation(
      summary = "Update a stock in the cart.",
      description = "Update the quantity of a stock. "
          + "Return every stocks in the cart, with their details and quantity.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Stock successfully updated.",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          implementation = ApiCartItemResponseDto.class
                      )
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Stock does not exist or quantity is invalid."
          ),
          @ApiResponse(
              responseCode = "401",
              description = "User is not logged in."
          )
      }
  )
  List<ApiCartItemResponseDto> updateStockInCart(
      @PathParam("title") String title,
      CartStockRequestDto cartStockRequestDto);

  @DELETE
  @Path("/{title}")
  @Operation(
      summary = "Delete a stock in the cart.",
      description = "Remove a stock from the cart. "
          + "Return every stocks in the cart, with their details and quantity.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Stock successfully deleted.",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          implementation = ApiCartItemResponseDto.class
                      )
                  )
              )
          ),
          @ApiResponse(
              responseCode = "401",
              description = "User is not logged in."
          )
      }
  )
  List<ApiCartItemResponseDto> deleteStockInCart(
      @PathParam("title") String title);

  @DELETE
  @Operation(
      summary = "Empty the cart.",
      description = "Remove all stocks from the cart.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Cart successfully emptied."
          ),
          @ApiResponse(
              responseCode = "401",
              description = "User is not logged in."
          )
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
              responseCode = "200",
              description = "Cart successfully checked out.",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          implementation = ApiTransactionDto.class
                      )
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Empty cart cannot be checked out."
          ),
          @ApiResponse(
              responseCode = "401",
              description = "User is not logged in."
          ),
          @ApiResponse(
              responseCode = "403",
              description = "Transaction exceed user limit."
          )
      }
  )
  ApiTransactionDto checkoutCart();
}
