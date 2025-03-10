package org.ainkai.usermgmt.api.service;

import org.ainkai.usermgmt.api.data.model.User;

public interface UserService {

  User findUserByToken(String token);

}
