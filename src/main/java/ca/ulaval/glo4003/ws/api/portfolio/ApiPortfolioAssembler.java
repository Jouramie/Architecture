package ca.ulaval.glo4003.ws.api.portfolio;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioItemDto;
import java.util.List;

@Component
public class ApiPortfolioAssembler {

  public ApiPortfolioResponseDto toDto(PortfolioDto responseDto) {
    List<ApiPortfolioItemResponseDto> apiItems = responseDto.stocks.stream().map(this::itemToDto).collect(toList());
    return new ApiPortfolioResponseDto(apiItems, responseDto.currentTotalValue);
  }

  private ApiPortfolioItemResponseDto itemToDto(PortfolioItemDto itemResponseDto) {

    return new ApiPortfolioItemResponseDto(itemResponseDto.title, itemResponseDto.currentValue, itemResponseDto.quantity);
  }
}
