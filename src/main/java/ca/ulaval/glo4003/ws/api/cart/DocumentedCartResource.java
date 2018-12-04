package ca.ulaval.glo4003.ws.api.cart;

import ca.ulaval.glo4003.ws.api.cart.dto.ApiCartItemResponseDto;
import ca.ulaval.glo4003.ws.api.cart.dto.ApiTransactionDto;
import ca.ulaval.glo4003.ws.api.cart.dto.CartStockRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;

public interface DocumentedCartResource {

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
              description = "Investor is not logged in."
          )
      }
  )
  List<ApiCartItemResponseDto> getCartContent();

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
              description = "Investor is not logged in."
          )
      }
  )
  List<ApiCartItemResponseDto> addStockToCart(String title, CartStockRequestDto cartStockRequestDto);

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
              description = "Investor is not logged in."
          )
      }
  )
  List<ApiCartItemResponseDto> updateStockInCart(String title, CartStockRequestDto cartStockRequestDto);

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
              description = "Investor is not logged in."
          )
      }
  )
  List<ApiCartItemResponseDto> deleteStockInCart(String title);

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
              description = "Investor is not logged in."
          )
      }
  )
  void emptyCart();

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
              description = "Investor is not logged in."
          ),
          @ApiResponse(
              responseCode = "403",
              description = "Transaction exceed user limit or one market of a stock is halted."
          )
      }
  )
  ApiTransactionDto checkoutCart();
}
