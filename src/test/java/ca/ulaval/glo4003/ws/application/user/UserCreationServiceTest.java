package ca.ulaval.glo4003.ws.application.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.ws.api.authentication.UserCreationDto;
import ca.ulaval.glo4003.ws.api.authentication.UserDto;
import ca.ulaval.glo4003.ws.domain.user.User;
import ca.ulaval.glo4003.ws.domain.user.UserFactory;
import ca.ulaval.glo4003.ws.domain.user.UserRepository;
import ca.ulaval.glo4003.ws.domain.user.UserRole;
import ca.ulaval.glo4003.ws.util.UserBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserCreationServiceTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final UserRole ROLE = UserRole.ADMINISTRATOR;
    public static final UserDto USER_DTO = new UserDto(USERNAME, ROLE);
    private static final UserCreationDto CREATION_REQUEST = new UserCreationDto(USERNAME, PASSWORD, ROLE);
    private static final User USER = new UserBuilder().build();

    @Mock
    private UserFactory userFactory;
    @Mock
    private UserAssembler userAssembler;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserCreationService service;

    @Test
    public void whenCreatingUser_thenUserIsCreatedAndSaved() {
        given(userFactory.create(USERNAME, PASSWORD, ROLE)).willReturn(USER);

        service.createUser(CREATION_REQUEST);

        verify(userFactory).create(USERNAME, PASSWORD, ROLE);
        verify(userRepository).save(USER);
    }

    @Test
    public void whenCreatingUser_thenReturnsUserDto() {
        given(userAssembler.toDto(any())).willReturn(USER_DTO);

        UserDto createdUser = service.createUser(CREATION_REQUEST);

        assertThat(createdUser).isEqualTo(USER_DTO);
    }

}
