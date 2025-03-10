package org.ainkai.usermgmt.api.data;

import org.ainkai.usermgmt.api.data.model.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {


  VerificationToken findByUserId(String userId);
}
