package org.ainkai.usermgmt.api.data;

import org.ainkai.usermgmt.api.data.model.Address;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AddressRepository extends MongoRepository<Address, String> {
}
