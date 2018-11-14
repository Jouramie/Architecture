package ca.ulaval.glo4003.ws.api.user;

import ca.ulaval.glo4003.ws.api.user.dto.ApiUserLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserMoneyAmountLimitCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserStockLimitCreationDto;
import javax.annotation.Resource;
import javax.validation.Valid;
import javax.ws.rs.core.Response;

@Resource
public class UserLimitResourceImpl implements UserLimitResource {

  @Override
  public ApiUserLimitDto setUserStockLimit(String email,
                                           @Valid UserStockLimitCreationDto userStockLimitCreationDto) {
    return null;
  }

  @Override
  public ApiUserLimitDto setUserAmountLimit(String email,
                                            @Valid UserMoneyAmountLimitCreationDto userMoneyAmountLimitCreationDto) {
    return null;
  }

  @Override
  public Response removeUserLimit(String email) {

    return Response.noContent().build();
  }
}
