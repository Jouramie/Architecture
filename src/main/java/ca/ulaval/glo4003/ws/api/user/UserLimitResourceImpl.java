package ca.ulaval.glo4003.ws.api.user;

import ca.ulaval.glo4003.ws.api.user.dto.ApiUserLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserLimitCreationDto;
import javax.annotation.Resource;
import javax.ws.rs.core.Response;

@Resource
public class UserLimitResourceImpl implements UserLimitResource {

  @Override
  public ApiUserLimitDto setUserLimit(String email,
                                      UserLimitCreationDto userLimitCreationDto) {

    return null;
  }

  @Override
  public Response removeUserLimit(String email) {

    return Response.noContent().build();
  }
}
