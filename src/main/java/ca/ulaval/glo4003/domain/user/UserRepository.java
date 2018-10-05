package ca.ulaval.glo4003.domain.user;

public interface UserRepository {
  void add(User user);

  void update(User user);

  User find(String email);
}
