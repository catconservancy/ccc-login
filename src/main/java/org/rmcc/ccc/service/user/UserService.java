package org.rmcc.ccc.service.user;

import java.util.Collection;
import java.util.Optional;

import org.rmcc.ccc.model.User;
import org.rmcc.ccc.model.UserCreateForm;

public interface UserService {

    Optional<User> getUserById(long id);

    Optional<User> getUserByEmail(String email);

    Collection<User> getAllUsers();

    User create(UserCreateForm form);

}