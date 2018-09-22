package ca.ulaval.glo4003.ws.domain.user;

import ca.ulaval.glo4003.ws.infrastructure.injection.Component;

@Component
public class UserFactory {

    public User create(String username, String password, UserRole role) {
        return new User(username, password, role);
    }
}
