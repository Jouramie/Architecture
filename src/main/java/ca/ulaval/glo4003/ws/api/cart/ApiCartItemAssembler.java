package ca.ulaval.glo4003.ws.api.cart;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.service.Component;
import ca.ulaval.glo4003.service.cart.dto.CartItemDto;
import java.util.List;

@Component
public class ApiCartItemAssembler {

  public List<ApiCartItemResponseDto> toDtoList(List<CartItemDto> cartItemDto) {
    return cartItemDto.stream().map(this::toDto).collect(toList());
  }

  public ApiCartItemResponseDto toDto(CartItemDto cartItemDto) {
    return new ApiCartItemResponseDto(cartItemDto.title,
        cartItemDto.market,
        cartItemDto.name,
        cartItemDto.category,
        cartItemDto.currentValue,
        cartItemDto.quantity);
  }
}
