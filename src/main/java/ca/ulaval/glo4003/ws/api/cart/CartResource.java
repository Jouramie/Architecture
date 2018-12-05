package ca.ulaval.glo4003.ws.api.cart;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.service.cart.CartService;
import ca.ulaval.glo4003.service.cart.CheckoutService;
import ca.ulaval.glo4003.service.cart.dto.TransactionDto;
import ca.ulaval.glo4003.ws.api.cart.assemblers.ApiCartItemAssembler;
import ca.ulaval.glo4003.ws.api.cart.assemblers.ApiTransactionAssembler;
import ca.ulaval.glo4003.ws.api.cart.dto.ApiCartItemResponseDto;
import ca.ulaval.glo4003.ws.api.cart.dto.ApiTransactionDto;
import ca.ulaval.glo4003.ws.api.cart.dto.CartStockRequestDto;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
import ca.ulaval.glo4003.ws.http.authentication.AuthenticationRequiredBinding;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;
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
@AuthenticationRequiredBinding(acceptedRoles = UserRole.INVESTOR)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Resource
public class CartResource implements DocumentedCartResource {
  private final CartService cartService;
  private final CheckoutService checkoutService;
  private final ApiTransactionAssembler apiTransactionAssembler;
  private final ApiCartItemAssembler apiCartItemAssembler;
  private final RequestValidator requestValidator;

  @Inject
  public CartResource(CartService cartService,
                      CheckoutService checkoutService,
                      ApiTransactionAssembler apiTransactionAssembler,
                      ApiCartItemAssembler apiCartItemAssembler) {
    this.cartService = cartService;
    this.checkoutService = checkoutService;
    this.apiTransactionAssembler = apiTransactionAssembler;
    this.apiCartItemAssembler = apiCartItemAssembler;
    requestValidator = new RequestValidator();
  }

  @GET
  @Override
  public List<ApiCartItemResponseDto> getCartContent() {
    return apiCartItemAssembler.toDtoList(cartService.getCartContent());
  }

  @PUT
  @Path("/{title}")
  @Override
  public List<ApiCartItemResponseDto> addStockToCart(@PathParam("title") String title,
                                                     CartStockRequestDto cartStockRequestDto) {
    requestValidator.validate(cartStockRequestDto);
    cartService.addStockToCart(title, cartStockRequestDto.quantity);

    return apiCartItemAssembler.toDtoList(cartService.getCartContent());
  }

  @PATCH
  @Path("/{title}")
  @Override
  public List<ApiCartItemResponseDto> updateStockInCart(@PathParam("title") String title,
                                                        CartStockRequestDto cartStockRequestDto) {
    requestValidator.validate(cartStockRequestDto);
    cartService.updateStockInCart(title, cartStockRequestDto.quantity);
    return apiCartItemAssembler.toDtoList(cartService.getCartContent());
  }

  @DELETE
  @Path("/{title}")
  @Override
  public List<ApiCartItemResponseDto> deleteStockInCart(@PathParam("title") String title) {
    cartService.removeStockFromCart(title);
    return apiCartItemAssembler.toDtoList(cartService.getCartContent());
  }

  @DELETE
  @Override
  public void emptyCart() {
    cartService.emptyCart();
  }

  @POST
  @Path("/checkout")
  @Consumes(MediaType.WILDCARD)
  @Override
  public ApiTransactionDto checkoutCart() {
    TransactionDto transactionDto = checkoutService.checkoutCart();
    return apiTransactionAssembler.toDto(transactionDto);
  }
}
