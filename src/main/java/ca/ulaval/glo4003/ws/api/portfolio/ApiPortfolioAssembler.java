package ca.ulaval.glo4003.ws.api.portfolio;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.service.portfolio.PortfolioItemResponseDto;
import ca.ulaval.glo4003.service.portfolio.PortfolioResponseDto;
import java.util.List;

@Component
public class ApiPortfolioAssembler {

  public ApiPortfolioResponseDto toDto(PortfolioResponseDto responseDto) {
    List<ApiPortfolioItemResponseDto> apiItems = responseDto.stocks.stream().map(this::itemToDto).collect(toList());
    return new ApiPortfolioResponseDto(apiItems, responseDto.currentTotalValue);
  }

  private ApiPortfolioItemResponseDto itemToDto(PortfolioItemResponseDto itemResponseDto) {

    return new ApiPortfolioItemResponseDto(itemResponseDto.title, itemResponseDto.currentValue, itemResponseDto.quantity);
  }
}
