package org.rmcc.ccc.repository;

import java.util.List;
import java.util.Optional;

import org.rmcc.ccc.model.User;
import org.rmcc.ccc.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findOneByEmail(String email);

	List<User> findByEnabled(Boolean valueOf);

	List<User> findAllByRole(Role role);

}
