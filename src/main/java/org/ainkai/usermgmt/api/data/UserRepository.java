package org.ainkai.usermgmt.api.data;

import org.ainkai.usermgmt.api.data.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {

  User findByEmail(String email);

  boolean existsByEmail(String email);
}
