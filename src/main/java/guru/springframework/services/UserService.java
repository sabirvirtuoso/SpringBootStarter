package guru.springframework.services;

import guru.springframework.domain.User;

public interface UserService extends CRUDService<User> {

    public User findByUserName(String userName);

}
